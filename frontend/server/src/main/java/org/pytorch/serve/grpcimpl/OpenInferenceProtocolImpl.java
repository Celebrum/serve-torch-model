package org.pytorch.serve.grpcimpl;

import com.google.gson.Gson;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.pytorch.serve.archive.model.ModelNotFoundException;
import org.pytorch.serve.archive.model.ModelVersionNotFoundException;
import org.pytorch.serve.grpcimpl.inference.*;
import org.pytorch.serve.http.BadRequestException;
import org.pytorch.serve.http.InternalServerException;
import org.pytorch.serve.job.GRPCJob;
import org.pytorch.serve.job.Job;
import org.pytorch.serve.util.ApiUtils;
import org.pytorch.serve.util.messages.InputParameter;
import org.pytorch.serve.util.messages.RequestInput;
import org.pytorch.serve.util.messages.WorkerCommands;
import org.pytorch.serve.wlm.Model;
import org.pytorch.serve.wlm.ModelManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OpenInferenceProtocolImpl extends InferenceServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(OpenInferenceProtocolImpl.class);
    private static final Gson GSON = new Gson();

    @Override
    public void serverLive(ServerLiveRequest request, StreamObserver<ServerLiveResponse> responseObserver) {
        ((ServerCallStreamObserver<ServerLiveResponse>) responseObserver)
                .setOnCancelHandler(() -> {
                    logger.warn("gRPC client call cancelled");
                    responseObserver.onError(
                            io.grpc.Status.CANCELLED
                                    .withDescription("call cancelled")
                                    .asRuntimeException());
                });

        ServerLiveResponse readyResponse = ServerLiveResponse.newBuilder().setLive(true).build();
        responseObserver.onNext(readyResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void serverReady(ServerReadyRequest request, StreamObserver<ServerReadyResponse> responseObserver) {
        ((ServerCallStreamObserver<ServerReadyResponse>) responseObserver)
                .setOnCancelHandler(() -> {
                    logger.warn("gRPC client call cancelled");
                    responseObserver.onError(
                            io.grpc.Status.CANCELLED
                                    .withDescription("call cancelled")
                                    .asRuntimeException());
                });

        ServerReadyResponse readyResponse = ServerReadyResponse.newBuilder().setReady(true).build();
        responseObserver.onNext(readyResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void modelReady(ModelReadyRequest request, StreamObserver<ModelReadyResponse> responseObserver) {
        ((ServerCallStreamObserver<ModelReadyResponse>) responseObserver)
                .setOnCancelHandler(() -> {
                    logger.warn("gRPC client call cancelled");
                    responseObserver.onError(
                            io.grpc.Status.CANCELLED
                                    .withDescription("call cancelled")
                                    .asRuntimeException());
                });

        String modelName = request.getName();
        String modelVersion = request.getVersion();
        boolean isModelReady = false;

        try {
            Model model = ModelManager.getInstance().getModel(modelName, modelVersion);
            if (model == null) {
                throw new ModelNotFoundException("Model not found: " + modelName);
            }
            isModelReady = true;
        } catch (ModelVersionNotFoundException | ModelNotFoundException e) {
            logger.warn("Model not found: {}", e.getMessage());
            isModelReady = false;
        }

        ModelReadyResponse modelReadyResponse = ModelReadyResponse.newBuilder().setReady(isModelReady).build();
        responseObserver.onNext(modelReadyResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void modelMetadata(
            ModelMetadataRequest request, StreamObserver<ModelMetadataResponse> responseObserver) {
        ((ServerCallStreamObserver<ModelMetadataResponse>) responseObserver)
                .setOnCancelHandler(() -> {
                    logger.warn("gRPC client call cancelled");
                    responseObserver.onError(
                            io.grpc.Status.CANCELLED
                                    .withDescription("call cancelled")
                                    .asRuntimeException());
                });

        String modelName = request.getName();
        String modelVersion = request.getVersion();

        ModelMetadataResponse.Builder response = ModelMetadataResponse.newBuilder();
        List<ModelMetadataResponse.TensorMetadata> inputs = new ArrayList<>();
        List<ModelMetadataResponse.TensorMetadata> outputs = new ArrayList<>();

        try {
            Model model = ModelManager.getInstance().getModel(modelName, modelVersion);
            if (model == null) {
                throw new ModelNotFoundException("Model not found: " + modelName);
            }

            response.setName(model.getModelName())
                    .setVersions(ModelManager.getInstance().getAllModelVersions(modelName))
                    .setPlatform("pytorch_torchserve");

            // TODO: Get actual input/output metadata from model

        } catch (ModelVersionNotFoundException | ModelNotFoundException e) {
            logger.warn("Model not found: {}", e.getMessage());
            responseObserver.onError(
                    io.grpc.Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException());
            return;
        }

        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void modelInfer(
            ModelInferRequest request, StreamObserver<ModelInferResponse> responseObserver) {
        ((ServerCallStreamObserver<ModelInferResponse>) responseObserver)
                .setOnCancelHandler(() -> {
                    logger.warn("gRPC client call cancelled");
                    responseObserver.onError(
                            io.grpc.Status.CANCELLED
                                    .withDescription("call cancelled")
                                    .asRuntimeException());
                });

        String modelName = request.getModelName();
        String modelVersion = request.getModelVersion();

        try {
            RequestInput input = new RequestInput(UUID.randomUUID().toString());
            for (ModelInferRequest.InferInputTensor entry : request.getInputsList()) {
                Map<String, Object> inferInputMap = new HashMap<>();
                addInputToMap(entry, inferInputMap);
                input.addParameter(new InputParameter(entry.getName(), inferInputMap));
            }

            Job job = new GRPCJob(modelName, modelVersion, WorkerCommands.PREDICT, input, responseObserver);
            if (!ModelManager.getInstance().addJob(job)) {
                throw new InternalServerException("Failed to add job to model worker");
            }
        } catch (ModelNotFoundException | ModelVersionNotFoundException e) {
            logger.warn("Model not found: {}", e.getMessage());
            responseObserver.onError(
                    io.grpc.Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException());
        } catch (BadRequestException | InternalServerException e) {
            logger.warn("Error processing request: {}", e.getMessage());
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription(e.getMessage())
                            .asRuntimeException());
        }
    }

    private void addInputToMap(
            ModelInferRequest.InferInputTensor inferInputTensor, Map<String, Object> inferInputMap) {
        InferTensorContents contents = inferInputTensor.getContents();
        if (contents != null) {
            // Add the appropriate content type based on the tensor datatype
            String dataType = inferInputTensor.getDatatype().toLowerCase();
            switch (dataType) {
                case "bool":
                    inferInputMap.put("data", contents.getBoolContents());
                    break;
                case "int32":
                    inferInputMap.put("data", contents.getIntContents());
                    break;
                case "int64":
                    inferInputMap.put("data", contents.getInt64Contents());
                    break;
                case "uint32":
                    inferInputMap.put("data", contents.getUintContents());
                    break;
                case "uint64":
                    inferInputMap.put("data", contents.getUint64Contents());
                    break;
                case "fp32":
                    inferInputMap.put("data", contents.getFp32Contents());
                    break;
                case "fp64":
                    inferInputMap.put("data", contents.getFp64Contents());
                    break;
                case "bytes":
                    inferInputMap.put("data", contents.getBytesContents());
                    break;
                default:
                    logger.warn("Unsupported datatype: {}", dataType);
                    break;
            }
        }
    }
}

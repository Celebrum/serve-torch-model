package org.pytorch.serve.grpcimpl.inference;

import io.grpc.stub.StreamObserver;

public abstract class InferenceServiceImpl {
    public abstract void serverLive(ServerLiveRequest request, StreamObserver<ServerLiveResponse> responseObserver);

    public abstract void serverReady(ServerReadyRequest request, StreamObserver<ServerReadyResponse> responseObserver);

    public abstract void modelReady(ModelReadyRequest request, StreamObserver<ModelReadyResponse> responseObserver);

    public abstract void modelMetadata(ModelMetadataRequest request,
            StreamObserver<ModelMetadataResponse> responseObserver);

    public abstract void modelInfer(ModelInferRequest request, StreamObserver<ModelInferResponse> responseObserver);
}
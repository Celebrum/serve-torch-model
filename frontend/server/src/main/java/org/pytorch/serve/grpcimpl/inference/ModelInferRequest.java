package org.pytorch.serve.grpcimpl.inference;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ModelInferRequest {
    private String modelName;
    private String modelVersion;
    private String id;
    private Map<String, InferParameter> parameters;
    private List<InferInputTensor> inputs;
    private List<InferRequestedOutputTensor> outputs;
    private List<byte[]> rawInputContents;

    public static class InferParameter {
        private Boolean boolParam;
        private Long int64Param;
        private String stringParam;

        public InferParameter(Boolean boolParam, Long int64Param, String stringParam) {
            this.boolParam = boolParam;
            this.int64Param = int64Param;
            this.stringParam = stringParam;
        }

        public Boolean getBoolParam() {
            return boolParam;
        }

        public Long getInt64Param() {
            return int64Param;
        }

        public String getStringParam() {
            return stringParam;
        }
    }

    public static class InferInputTensor {
        private String name;
        private String datatype;
        private List<Long> shape;
        private Map<String, InferParameter> parameters;
        private InferTensorContents contents;

        public InferInputTensor(String name, String datatype, List<Long> shape,
                Map<String, InferParameter> parameters, InferTensorContents contents) {
            this.name = name;
            this.datatype = datatype;
            this.shape = shape;
            this.parameters = parameters;
            this.contents = contents;
        }

        public String getName() {
            return name;
        }

        public String getDatatype() {
            return datatype;
        }

        public List<Long> getShape() {
            return shape;
        }

        public Map<String, InferParameter> getParameters() {
            return parameters;
        }

        public InferTensorContents getContents() {
            return contents;
        }
    }

    public static class InferRequestedOutputTensor {
        private String name;
        private Map<String, InferParameter> parameters;

        public InferRequestedOutputTensor(String name, Map<String, InferParameter> parameters) {
            this.name = name;
            this.parameters = parameters;
        }

        public String getName() {
            return name;
        }

        public Map<String, InferParameter> getParameters() {
            return parameters;
        }
    }

    public ModelInferRequest(String modelName, String modelVersion, String id,
            Map<String, InferParameter> parameters,
            List<InferInputTensor> inputs,
            List<InferRequestedOutputTensor> outputs,
            List<byte[]> rawInputContents) {
        this.modelName = modelName;
        this.modelVersion = modelVersion;
        this.id = id;
        this.parameters = parameters;
        this.inputs = inputs;
        this.outputs = outputs;
        this.rawInputContents = rawInputContents;
    }

    public String getModelName() {
        return modelName;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public String getId() {
        return id;
    }

    public Map<String, InferParameter> getParameters() {
        return parameters;
    }

    public List<InferInputTensor> getInputsList() {
        return inputs;
    }

    public List<InferRequestedOutputTensor> getOutputs() {
        return outputs;
    }

    public List<byte[]> getRawInputContents() {
        return rawInputContents;
    }
}
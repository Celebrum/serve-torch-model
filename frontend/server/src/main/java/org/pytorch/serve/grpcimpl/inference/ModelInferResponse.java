package org.pytorch.serve.grpcimpl.inference;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ModelInferResponse {
    private String modelName;
    private String modelVersion;
    private String id;
    private Map<String, ModelInferRequest.InferParameter> parameters;
    private List<InferOutputTensor> outputs;
    private List<byte[]> rawOutputContents;

    public static class InferOutputTensor {
        private String name;
        private String datatype;
        private List<Long> shape;
        private Map<String, ModelInferRequest.InferParameter> parameters;
        private InferTensorContents contents;

        public InferOutputTensor(String name, String datatype, List<Long> shape,
                               Map<String, ModelInferRequest.InferParameter> parameters,
                               InferTensorContents contents) {
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

        public Map<String, ModelInferRequest.InferParameter> getParameters() {
            return parameters;
        }

        public InferTensorContents getContents() {
            return contents;
        }

        public static Builder newBuilder() {
            return new Builder();
        }

        public static class Builder {
            private String name;
            private String datatype;
            private List<Long> shape = new ArrayList<>();
            private Map<String, ModelInferRequest.InferParameter> parameters = new HashMap<>();
            private InferTensorContents contents;

            public Builder setName(String name) {
                this.name = name;
                return this;
            }

            public Builder setDatatype(String datatype) {
                this.datatype = datatype;
                return this;
            }

            public Builder setShape(List<Long> shape) {
                this.shape = shape;
                return this;
            }

            public Builder setParameters(Map<String, ModelInferRequest.InferParameter> parameters) {
                this.parameters = parameters;
                return this;
            }

            public Builder setContents(InferTensorContents contents) {
                this.contents = contents;
                return this;
            }

            public InferOutputTensor build() {
                return new InferOutputTensor(name, datatype, shape, parameters, contents);
            }
        }
    }

    public ModelInferResponse(String modelName, String modelVersion, String id,
                            Map<String, ModelInferRequest.InferParameter> parameters,
                            List<InferOutputTensor> outputs,
                            List<byte[]> rawOutputContents) {
        this.modelName = modelName;
        this.modelVersion = modelVersion;
        this.id = id;
        this.parameters = parameters;
        this.outputs = outputs;
        this.rawOutputContents = rawOutputContents;
    }

    public static Builder newBuilder() {
        return new Builder();
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

    public Map<String, ModelInferRequest.InferParameter> getParameters() {
        return parameters;
    }

    public List<InferOutputTensor> getOutputs() {
        return outputs;
    }

    public List<byte[]> getRawOutputContents() {
        return rawOutputContents;
    }

    public static class Builder {
        private String modelName;
        private String modelVersion;
        private String id;
        private Map<String, ModelInferRequest.InferParameter> parameters = new HashMap<>();
        private List<InferOutputTensor> outputs = new ArrayList<>();
        private List<byte[]> rawOutputContents = new ArrayList<>();

        public Builder setModelName(String modelName) {
            this.modelName = modelName;
            return this;
        }

        public Builder setModelVersion(String modelVersion) {
            this.modelVersion = modelVersion;
            return this;
        }

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setParameters(Map<String, ModelInferRequest.InferParameter> parameters) {
            this.parameters = parameters;
            return this;
        }

        public Builder setOutputs(List<InferOutputTensor> outputs) {
            this.outputs = outputs;
            return this;
        }

        public Builder setRawOutputContents(List<byte[]> rawOutputContents) {
            this.rawOutputContents = rawOutputContents;
            return this;
        }

        public ModelInferResponse build() {
            return new ModelInferResponse(modelName, modelVersion, id, parameters, outputs, rawOutputContents);
        }
    }
}
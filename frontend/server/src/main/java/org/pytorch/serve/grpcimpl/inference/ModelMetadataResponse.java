package org.pytorch.serve.grpcimpl.inference;

import java.util.List;
import java.util.ArrayList;

public class ModelMetadataResponse {
    private String name;
    private List<String> versions;
    private String platform;
    private List<TensorMetadata> inputs;
    private List<TensorMetadata> outputs;

    public static class TensorMetadata {
        private String name;
        private String datatype;
        private List<Long> shape;

        public TensorMetadata(String name, String datatype, List<Long> shape) {
            this.name = name;
            this.datatype = datatype;
            this.shape = shape;
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

        public static Builder newBuilder() {
            return new Builder();
        }

        public static class Builder {
            private String name;
            private String datatype;
            private List<Long> shape = new ArrayList<>();

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

            public TensorMetadata build() {
                return new TensorMetadata(name, datatype, shape);
            }
        }
    }

    public ModelMetadataResponse(String name, List<String> versions, String platform,
                               List<TensorMetadata> inputs, List<TensorMetadata> outputs) {
        this.name = name;
        this.versions = versions;
        this.platform = platform;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public List<String> getVersions() {
        return versions;
    }

    public String getPlatform() {
        return platform;
    }

    public List<TensorMetadata> getInputs() {
        return inputs;
    }

    public List<TensorMetadata> getOutputs() {
        return outputs;
    }

    public static class Builder {
        private String name;
        private List<String> versions = new ArrayList<>();
        private String platform;
        private List<TensorMetadata> inputs = new ArrayList<>();
        private List<TensorMetadata> outputs = new ArrayList<>();

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setVersions(List<String> versions) {
            this.versions = versions;
            return this;
        }

        public Builder setPlatform(String platform) {
            this.platform = platform;
            return this;
        }

        public Builder setInputs(List<TensorMetadata> inputs) {
            this.inputs = inputs;
            return this;
        }

        public Builder setOutputs(List<TensorMetadata> outputs) {
            this.outputs = outputs;
            return this;
        }

        public ModelMetadataResponse build() {
            return new ModelMetadataResponse(name, versions, platform, inputs, outputs);
        }
    }
}
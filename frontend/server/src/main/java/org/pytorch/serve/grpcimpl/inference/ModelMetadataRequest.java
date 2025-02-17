package org.pytorch.serve.grpcimpl.inference;

public class ModelMetadataRequest {
    private String name;
    private String version;

    public ModelMetadataRequest(String name, String version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
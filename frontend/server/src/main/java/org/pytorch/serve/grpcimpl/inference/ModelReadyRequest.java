package org.pytorch.serve.grpcimpl.inference;

public class ModelReadyRequest {
    private String name;
    private String version;

    public ModelReadyRequest(String name, String version) {
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
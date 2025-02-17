package org.pytorch.serve.grpcimpl.inference;

public class ModelReadyResponse {
    private boolean ready;

    public ModelReadyResponse(boolean ready) {
        this.ready = ready;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public boolean getReady() {
        return ready;
    }

    public static class Builder {
        private boolean ready;

        public Builder setReady(boolean ready) {
            this.ready = ready;
            return this;
        }

        public ModelReadyResponse build() {
            return new ModelReadyResponse(ready);
        }
    }
}
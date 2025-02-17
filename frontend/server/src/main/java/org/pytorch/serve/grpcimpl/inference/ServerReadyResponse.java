package org.pytorch.serve.grpcimpl.inference;

public class ServerReadyResponse {
    private boolean ready;

    public ServerReadyResponse(boolean ready) {
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

        public ServerReadyResponse build() {
            return new ServerReadyResponse(ready);
        }
    }
}
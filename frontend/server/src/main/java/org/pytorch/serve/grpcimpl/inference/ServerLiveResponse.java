package org.pytorch.serve.grpcimpl.inference;

public class ServerLiveResponse {
    private boolean live;

    public ServerLiveResponse(boolean live) {
        this.live = live;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public boolean getLive() {
        return live;
    }

    public static class Builder {
        private boolean live;

        public Builder setLive(boolean live) {
            this.live = live;
            return this;
        }

        public ServerLiveResponse build() {
            return new ServerLiveResponse(live);
        }
    }
}
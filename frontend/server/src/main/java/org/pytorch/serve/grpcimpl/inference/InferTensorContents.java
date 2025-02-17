package org.pytorch.serve.grpcimpl.inference;

import java.util.List;
import java.util.ArrayList;

public class InferTensorContents {
    private List<Boolean> boolContents;
    private List<Integer> intContents;
    private List<Long> int64Contents;
    private List<Integer> uintContents;
    private List<Long> uint64Contents;
    private List<Float> fp32Contents;
    private List<Double> fp64Contents;
    private List<byte[]> bytesContents;

    public InferTensorContents() {
        this.boolContents = new ArrayList<>();
        this.intContents = new ArrayList<>();
        this.int64Contents = new ArrayList<>();
        this.uintContents = new ArrayList<>();
        this.uint64Contents = new ArrayList<>();
        this.fp32Contents = new ArrayList<>();
        this.fp64Contents = new ArrayList<>();
        this.bytesContents = new ArrayList<>();
    }

    public List<Boolean> getBoolContents() {
        return boolContents;
    }

    public List<Integer> getIntContents() {
        return intContents;
    }

    public List<Long> getInt64Contents() {
        return int64Contents;
    }

    public List<Integer> getUintContents() {
        return uintContents;
    }

    public List<Long> getUint64Contents() {
        return uint64Contents;
    }

    public List<Float> getFp32Contents() {
        return fp32Contents;
    }

    public List<Double> getFp64Contents() {
        return fp64Contents;
    }

    public List<byte[]> getBytesContents() {
        return bytesContents;
    }
}
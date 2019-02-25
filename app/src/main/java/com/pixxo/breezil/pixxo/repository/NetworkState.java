package com.pixxo.breezil.pixxo.repository;

public class NetworkState {
    public enum Status {
        RUNNING,
        SUCCESS,
        FAILED,
        NO_RESULT
    }

    private final Status mStatus;

    public static final NetworkState LOADED;
    public static final NetworkState LOADING;

    public NetworkState(Status status) {
        mStatus = status;
    }

    static {
        LOADED = new NetworkState(Status.SUCCESS);
        LOADING = new NetworkState(Status.RUNNING);
    }

    public Status getStatus() {
        return mStatus;
    }
}


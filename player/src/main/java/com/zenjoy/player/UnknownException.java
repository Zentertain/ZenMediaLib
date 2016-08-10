package com.zenjoy.player;

public class UnknownException extends RuntimeException {
    public UnknownException() {
    }

    public UnknownException(String detailMessage) {
        super(detailMessage);
    }
}

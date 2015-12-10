package com.github.bumblebee.command.bingpics;

public class ImageSendException extends Exception {

    public ImageSendException(Throwable cause) {
        super(cause);
    }

    public ImageSendException(String message, Throwable cause) {
        super(message, cause);
    }
}

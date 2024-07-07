package br.com.tech.challenge.api.exception;

public class QRCodeGenerationException extends RuntimeException {

    public QRCodeGenerationException(String message) {
        super(message);
    }

}

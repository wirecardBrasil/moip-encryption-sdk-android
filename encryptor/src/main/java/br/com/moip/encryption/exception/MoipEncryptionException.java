package br.com.moip.encryption.exception;


public class MoipEncryptionException extends Exception {

    public MoipEncryptionException() {
        super();
    }

    public MoipEncryptionException(final String message) {
        super(message);
    }

    public MoipEncryptionException(final Throwable cause) {
        super(cause);
    }

    public MoipEncryptionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MoipEncryptionException(final MoipEncryptionException mee) {
        super(mee);
    }
}

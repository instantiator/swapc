package swapc.lib.search.result;

public class SwapCertException extends Exception {

    public SwapCertException() {
    }

    public SwapCertException(String message) {
        super(message);
    }

    public SwapCertException(String message, Throwable cause) {
        super(message, cause);
    }

    public SwapCertException(Throwable cause) {
        super(cause);
    }

    public SwapCertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

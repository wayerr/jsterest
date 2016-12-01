package wayerr.jsterest;

/**
 * It an exception, but name as error for js compatibility
 */
public class AssertionError extends RuntimeException {
    public AssertionError() {
    }

    public AssertionError(String message) {
        super(message);
    }

    public AssertionError(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertionError(Throwable cause) {
        super(cause);
    }
}

package capston.cau.exception;

public class AccessTokenInvalidatedException extends RuntimeException{

    public AccessTokenInvalidatedException() {
    }

    public AccessTokenInvalidatedException(String message) {
        super(message);
    }

    public AccessTokenInvalidatedException(String message, Throwable cause) {
        super(message, cause);
    }
}

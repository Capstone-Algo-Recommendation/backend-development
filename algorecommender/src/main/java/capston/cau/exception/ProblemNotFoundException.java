package capston.cau.exception;

public class ProblemNotFoundException extends RuntimeException{
    public ProblemNotFoundException() {
        super();
    }

    public ProblemNotFoundException(String message) {
        super(message);
    }

    public ProblemNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

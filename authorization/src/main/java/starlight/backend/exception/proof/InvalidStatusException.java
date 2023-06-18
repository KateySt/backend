package starlight.backend.exception.proof;

public class InvalidStatusException extends RuntimeException {
    public InvalidStatusException(String status) {
        super("Status " + status + " is invalid");
    }
}

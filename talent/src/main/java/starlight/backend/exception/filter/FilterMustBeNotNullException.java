package starlight.backend.exception.filter;

public class FilterMustBeNotNullException extends RuntimeException {
    public FilterMustBeNotNullException() {
        String message = "Filter must not be null";
    }
}

package starlight.backend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import starlight.backend.exception.AuthorizationFailureException;
import starlight.backend.exception.EmailAlreadyOccupiedException;
import starlight.backend.exception.YouAreInDeletingProcess;
import starlight.backend.exception.user.*;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler({})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO badRequest(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler({
            EmailAlreadyOccupiedException.class,
            EmailAlreadyOccupiedException.class,
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorDTO alreadyIs(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler({
            AuthorizationFailureException.class,
            UserAccesDeniedToDeleteThisUserException.class,
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorDTO authFailure(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler({
            UserNotFoundInDelayedDeleteRepository.class,
            UserNotFoundWithUUIDException.class,
            UserNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO notExists(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler({
            YouAreInDeletingProcess.class,
            UserCanNotEditThisProfile.class,
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDTO forbidden(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    record ErrorDTO(String message) {
    }
}
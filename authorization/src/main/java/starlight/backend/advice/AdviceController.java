package starlight.backend.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import starlight.backend.exception.AuthorizationFailureException;
import starlight.backend.exception.EmailAlreadyOccupiedException;
import starlight.backend.exception.PageNotFoundException;
import starlight.backend.exception.YouAreInDeletingProcess;
import starlight.backend.exception.kudos.*;
import starlight.backend.exception.proof.InvalidStatusException;
import starlight.backend.exception.proof.ProofNotFoundException;
import starlight.backend.exception.proof.UserAccesDeniedToProofException;
import starlight.backend.exception.proof.UserCanNotEditProofNotInDraftException;
import starlight.backend.exception.user.*;
import starlight.backend.exception.user.sponsor.SponsorAlreadyOnDeleteList;
import starlight.backend.exception.user.sponsor.SponsorCanNotSeeAnotherSponsor;
import starlight.backend.exception.user.sponsor.SponsorNotFoundException;
import starlight.backend.exception.user.talent.TalentNotFoundException;

@RestControllerAdvice
public class AdviceController {

    @ExceptionHandler({
            PageNotFoundException.class,
            UserCanNotEditProofNotInDraftException.class,
            UserAccesDeniedToProofException.class,
            InvalidStatusException.class,
            UserCannotAddKudosToTheirAccount.class,
            KudosRequestMustBeNotZeroException.class,
            InvalidStatusException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO badRequest(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler({
            EmailAlreadyOccupiedException.class,
            ProofAlreadyHaveKudosFromUser.class,
            SponsorAlreadyOnDeleteList.class,
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
            ProofNotFoundException.class,
            TalentNotFoundException.class,
            UserNotFoundInDelayedDeleteRepository.class,
            UserNotFoundWithUUIDException.class,
            SponsorNotFoundException.class,
            UserNotFoundException.class,
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDTO notExists(Exception exception) {
        return new ErrorDTO(exception.getMessage());
    }

    @ExceptionHandler({
            TalentCanNotAddKudos.class,
            NotEnoughKudosException.class,
            SponsorCanNotSeeAnotherSponsor.class,
            YouCanNotReturnMoreKudosThanGaveException.class,
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
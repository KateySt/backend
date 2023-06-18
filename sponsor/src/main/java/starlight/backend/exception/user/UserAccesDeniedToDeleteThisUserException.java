package starlight.backend.exception.user;

public class UserAccesDeniedToDeleteThisUserException extends RuntimeException{
    public UserAccesDeniedToDeleteThisUserException(String userId) {
        super("User cannot delete this user" + userId);
    }
    public UserAccesDeniedToDeleteThisUserException(Long userId) {
        super("User cannot delete this user. UserId = " + userId);
    }
}

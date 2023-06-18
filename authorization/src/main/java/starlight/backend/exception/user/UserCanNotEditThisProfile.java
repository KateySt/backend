package starlight.backend.exception.user;

public class UserCanNotEditThisProfile extends RuntimeException {
    public UserCanNotEditThisProfile(long id) {
        super("User can not edit this profile. Target profile id = " + id);
    }

    public UserCanNotEditThisProfile(String id) {
        super("User can not edit this profile. Target profile id = " + id);
    }
}
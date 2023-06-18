package starlight.backend.user.model.response;

public record Talent(
        Long talentId,
        String email,
        String password
) {
}

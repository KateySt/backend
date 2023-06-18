package starlight.backend.user.model.response;

import lombok.Builder;

@Builder
public record Talent(
        long talent_id,
        String email,
        String password
) {
}

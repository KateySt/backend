package starlight.backend.talent.model.response;

import lombok.Builder;

@Builder
public record Talent(
        long talentId,
        String email,
        String password
) {
}

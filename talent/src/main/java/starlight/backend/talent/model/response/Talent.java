package starlight.backend.talent.model.response;

import lombok.Builder;

@Builder
public record Talent(
        Long talentId,
        String email,
        String password
) {
}

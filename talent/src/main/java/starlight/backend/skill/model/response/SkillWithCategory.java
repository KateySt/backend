package starlight.backend.skill.model.response;

import lombok.Builder;

@Builder
public record SkillWithCategory(
        long skillId,
        String skill,
        String category
) {
}

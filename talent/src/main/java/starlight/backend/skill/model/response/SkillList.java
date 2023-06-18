package starlight.backend.skill.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record SkillList(
        List<SkillWithCategory> skills
) {
}

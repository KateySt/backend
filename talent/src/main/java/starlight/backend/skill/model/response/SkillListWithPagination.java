package starlight.backend.skill.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record SkillListWithPagination(
        long total,
        List<SkillWithCategory> data
) {
}

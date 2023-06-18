package starlight.backend.talent.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record TalentPagePaginationWithFilterSkills(
        long total,
        List<TalentWithSkills> data
) {}
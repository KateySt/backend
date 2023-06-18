package starlight.backend.proof.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import starlight.backend.proof.model.enums.Status;
import starlight.backend.skill.model.response.SkillWithCategory;

import java.time.Instant;
import java.util.List;

@Builder
public record ProofWithSkills(
        String title,
        String description,
        String link,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        Instant dateCreated,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
        Instant dateLastUpdated,
        Status status,
        List<SkillWithCategory> skill
) {
}

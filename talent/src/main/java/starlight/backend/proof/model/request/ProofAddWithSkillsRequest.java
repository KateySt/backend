package starlight.backend.proof.model.request;

import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import java.util.List;

@Builder
public record ProofAddWithSkillsRequest(
        @Length(max = 255)
        String title,
        @Length(max = 1000)
        String description,
        @URL
        String link,
        List<String> skills
) {
}

package starlight.backend.skill.model.request;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record DeleteIdSkills(
        ArrayList<Long> skillsId
) {
}

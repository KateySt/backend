package starlight.backend.proof.model.response;

import lombok.Builder;

import java.util.List;

@Builder
public record ProofPagePaginationWithSkills(
        long total,
        List<ProofInfoWithSkills> data
) {
}
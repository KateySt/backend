package starlight.backend.proof.service;

import org.springframework.http.ResponseEntity;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.request.ProofAddRequest;
import starlight.backend.proof.model.response.ProofPagePagination;

public interface ProofServiceInterface {
    ProofPagePagination proofsPagination(int page, int size, boolean sort);

    ProofEntity addProofProfile(long talentId, ProofAddRequest proofUpdateRequest);
    ResponseEntity<?> getLocation(long talentId, ProofAddRequest proofAddRequest);
    void deleteProof(long talentId, long proofId);
}
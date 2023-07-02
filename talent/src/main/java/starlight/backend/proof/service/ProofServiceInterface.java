package starlight.backend.proof.service;


import org.springframework.http.ResponseEntity;
import starlight.backend.proof.model.entity.ProofEntity;
import starlight.backend.proof.model.request.ProofAddRequest;
import starlight.backend.proof.model.request.ProofAddWithSkillsRequest;
import starlight.backend.proof.model.request.ProofUpdateRequest;
import starlight.backend.proof.model.response.ProofFullInfo;
import starlight.backend.proof.model.response.ProofFullInfoWithSkills;
import starlight.backend.proof.model.response.ProofPagePagination;
import starlight.backend.proof.model.response.ProofPagePaginationWithSkills;

public interface ProofServiceInterface {
    ProofPagePagination proofsPagination(int page, int size, boolean sort);

    ProofEntity addProofProfile(long talentId, ProofAddRequest proofUpdateRequest);

    ResponseEntity<?> getLocation(long talentId, ProofAddRequest proofAddRequest);

    void deleteProof(long talentId, long proofId);

    ProofPagePagination getTalentAllProofs(long talentId, int page, int size, boolean sort, String status);

    ProofFullInfo getProofFullInfo(long proofId);

    ProofFullInfo proofUpdateRequest(long talentId, long id, ProofUpdateRequest proofUpdateRequest);

    ProofPagePaginationWithSkills getTalentAllProofsWithSkills(long talentId,
                                                               int page, int size, boolean sort, String status);

    ProofFullInfoWithSkills getProofFullInfoWithSkills(long proofId);

    ProofPagePaginationWithSkills proofsPaginationWithSkills(int page, int size, boolean sort);

    ResponseEntity<?> getLocationForAddProofWithSkill(long talentId, ProofAddWithSkillsRequest proofAddWithSkillsRequest);

    long addProofProfileWithSkill(long talentId, ProofAddWithSkillsRequest proofAddWithSkillsRequest);

    ProofPagePagination getTalentAllProofsWithKudoses(long talentId,
                                                      int page, int size, boolean sort, String status);

}

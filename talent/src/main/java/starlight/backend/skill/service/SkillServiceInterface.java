package starlight.backend.skill.service;

import org.springframework.security.core.Authentication;
import starlight.backend.proof.model.response.ProofListWithSkills;
import starlight.backend.proof.model.response.ProofWithSkills;
import starlight.backend.skill.model.entity.SkillEntity;
import starlight.backend.skill.model.request.AddSkill;
import starlight.backend.skill.model.request.DeleteIdSkills;
import starlight.backend.skill.model.response.SkillList;
import starlight.backend.skill.model.response.SkillListWithPagination;
import starlight.backend.talent.model.response.TalentWithSkills;

import java.util.List;

public interface SkillServiceInterface {
    SkillListWithPagination getListSkillWithFiltration(String filter, int skip, int limit);

    ProofWithSkills addSkillInYourProof(long talentId, long proofId, Authentication auth, AddSkill skills);

    SkillList getListSkillsOfProof(long proofId);

    List<SkillEntity> existsSkill(List<SkillEntity> proofSkill, List<String> skills);

    SkillEntity skillValidation(String skill);

    void deleteSkill(long talentId, long proofId, long skillId, Authentication auth);

    TalentWithSkills addSkillToTalent(long talentId, AddSkill skills, Authentication auth);

    ProofWithSkills addSkillInYourProofV2(long talentId, long proofId, Authentication auth, AddSkill skills);

    TalentWithSkills getListSkillsOfTalent(long talentId, Authentication auth);

    ProofListWithSkills getListProofsOfSkill(long talentId, long skillsId, String status, Authentication auth);

    void deleteSkillArray(long talentId, long proofId, DeleteIdSkills skillId, Authentication auth);

    void deleteSkills(long talentId, DeleteIdSkills skillId, Authentication auth);
}

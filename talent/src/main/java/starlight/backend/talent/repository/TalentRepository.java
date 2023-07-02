package starlight.backend.talent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.talent.model.entity.TalentEntity;

@Repository
public interface TalentRepository extends JpaRepository<TalentEntity, Long> {
    boolean existsByTalentId(Long talentId);
    TalentEntity findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByTalentSkills_SkillId(Long skillId);
}

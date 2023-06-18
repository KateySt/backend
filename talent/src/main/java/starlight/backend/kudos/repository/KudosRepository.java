package starlight.backend.kudos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import starlight.backend.kudos.model.entity.KudosEntity;

@Repository
public interface KudosRepository extends JpaRepository<KudosEntity, Long> {
}

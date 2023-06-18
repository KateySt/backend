package starlight.backend.kudos.service;

import org.springframework.security.core.Authentication;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.kudos.model.response.KudosOnProof;

public interface KudosServiceInterface {
    KudosOnProof getKudosOnProof(long proofId, Authentication auth);

    KudosEntity addKudosOnProof(long proofId, int kudosRequest, Authentication auth);
}

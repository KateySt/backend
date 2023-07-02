package starlight.backend.kudos.service;


import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.kudos.model.response.KudosOnProof;

public interface KudosServiceInterface {
    KudosOnProof getKudosOnProof(long proofId);

    KudosEntity addKudosOnProof(long proofId, int kudosRequest);
}

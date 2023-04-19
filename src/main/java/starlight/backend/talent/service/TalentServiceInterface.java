package starlight.backend.talent.service;

import org.springframework.security.core.Authentication;
import starlight.backend.talent.model.request.TalentUpdateRequest;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;

import java.util.Optional;

public interface TalentServiceInterface {
    TalentPagePagination talentPagination(int page, int size);

    Optional<TalentFullInfo> talentFullInfo(long id);

    TalentFullInfo updateTalentProfile(long id, TalentUpdateRequest talentUpdateRequest,Authentication auth);

    void deleteTalentProfile(long talentId, Authentication auth);
}
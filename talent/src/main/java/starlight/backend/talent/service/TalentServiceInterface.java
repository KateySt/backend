package starlight.backend.talent.service;

import org.springframework.security.core.Authentication;
import starlight.backend.talent.model.request.NewUser;
import starlight.backend.talent.model.request.TalentUpdateRequest;
import starlight.backend.talent.model.response.Talent;
import starlight.backend.talent.model.response.TalentFullInfo;
import starlight.backend.talent.model.response.TalentPagePagination;
import starlight.backend.talent.model.response.TalentPagePaginationWithFilterSkills;

public interface TalentServiceInterface {
    TalentPagePagination talentPagination(int page, int size);

    TalentFullInfo talentFullInfo(long id);

    TalentFullInfo updateTalentProfile(long id, TalentUpdateRequest talentUpdateRequest, Authentication auth);

    void deleteTalentProfile(long talentId, Authentication auth);

    TalentPagePaginationWithFilterSkills talentPaginationWithFilter(String filter, int skip, int limit);

    void isStatusCorrect(String status);
    Talent saveTalent(NewUser user);

    Talent getTalentByEmail(String email);
    boolean checkingLoggedAndToken(long userId, Authentication auth);
}
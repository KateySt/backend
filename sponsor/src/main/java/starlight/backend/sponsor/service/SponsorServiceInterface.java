package starlight.backend.sponsor.service;

import org.springframework.http.ResponseEntity;
import starlight.backend.sponsor.model.request.SponsorUpdateRequest;
import starlight.backend.sponsor.model.response.SponsorFullInfo;
import starlight.backend.sponsor.model.response.SponsorKudosInfo;

public interface SponsorServiceInterface {
    SponsorKudosInfo getUnusableKudos(long sponsorId);

    SponsorFullInfo getSponsorFullInfo(long sponsorId);

    SponsorFullInfo updateSponsorProfile(long id, SponsorUpdateRequest sponsorUpdateRequest);

    ResponseEntity<String> deleteSponsor(long sponsorId);

    String getSponsorMail(long sponsorId);

    ResponseEntity<String> sendEmailForRecoverySponsorAccount(long sponsorId);
}

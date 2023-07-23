package starlight.backend.sponsor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import starlight.backend.sponsor.model.request.SponsorUpdateRequest;
import starlight.backend.sponsor.model.response.SponsorFullInfo;
import starlight.backend.sponsor.model.response.SponsorKudosInfo;
import starlight.backend.sponsor.service.SponsorServiceInterface;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
@Slf4j
@Tag(name = "Sponsor", description = "Sponsor related endpoints")
public class SponsorController {
    private SponsorServiceInterface sponsorService;

    @Operation(
            summary = "Get unusable Sponsor's kudos",
            description = "Get unusable Sponsor's kudos"
    )
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = SponsorKudosInfo.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @GetMapping("/sponsors/{sponsor-id}/kudos")
    public SponsorKudosInfo getUnusableKudosForSponsor(@PathVariable("sponsor-id") long sponsorId) {
        log.info("@GetMapping(\"/sponsors/{sponsor-id}/kudos\")");
        return sponsorService.getUnusableKudos(sponsorId);
    }

    @Operation(
            summary = "Get Sponsor full info",
            description = "Get Sponsor full info"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    }
    )
    @GetMapping("/sponsors/{sponsor-id}")
    public SponsorFullInfo sponsorFullInfo(@PathVariable("sponsor-id") long sponsorId) {
        log.info("@GetMapping(\"/sponsors/{sponsor-id}\")");
        return sponsorService.getSponsorFullInfo(sponsorId);
    }

    @Operation(summary = "Update sponsor by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = SponsorFullInfo.class
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "409", description = "Conflict")
    })
    @PatchMapping("/sponsors/{sponsor-id}")
    public SponsorFullInfo updateSponsorFullInfo(@PathVariable("sponsor-id") long sponsorId,
                                                 @RequestBody SponsorUpdateRequest sponsorUpdateRequest) {
        log.info("@PatchMapping(\"/sponsors/{sponsor-id}\")");
        return sponsorService.updateSponsorProfile(sponsorId, sponsorUpdateRequest);
    }

    @Operation(
            summary = "Delete sponsor",
            description = "Deletes a sponsor by the specified identifier. Only users with the 'ROLE_SPONSOR' role can use this endpoint.",
            tags = {"Sponsor", "Delete"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful sponsor deletion"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Sponsor not found")
    })
    @Tag(name = "Delete", description = "Delete sponsor")
    @DeleteMapping("/sponsors/{sponsor-id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteSponsor(@PathVariable("sponsor-id") long sponsorId) {
        log.info("@DeleteMapping(\"/sponsors/{sponsor-id}\")");
        return sponsorService.deleteSponsor(sponsorId);
    }

    @Operation(
            summary = "Send email for recovery sponsor account",
            description = "Send email for recovery sponsor account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful sponsor deletion"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Sponsor not found")
    }
    )
    @Tag(name = "Delete")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/sponsors/{sponsor-id}/send-recovery-account-email")
    public ResponseEntity<String> sendEmailForRecoverySponsorAccount(@PathVariable("sponsor-id") long sponsorId) {
        log.info("@PostMapping(\"/sponsors/{sponsor-id}/send-recovery-account-email\")");
        return sponsorService.sendEmailForRecoverySponsorAccount(sponsorId);
    }
}

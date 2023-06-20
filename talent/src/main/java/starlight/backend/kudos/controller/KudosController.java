package starlight.backend.kudos.controller;

/*
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/v1")
@Validated
@Tag(name = "Kudos", description = "Kudos API")
@RestController
public class KudosController {
    private KudosServiceInterface kudosService;

    @Operation(
            summary = "Get all kudos on proof",
            description = "Get all kudos on proof and boolean flag if you as a Sponsor already kudosed this proof"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = ProofNotFoundException.class
                            )
                    )
            )
    })
    @GetMapping("/proofs/{proof-id}/kudos")
    public KudosOnProof getKudosOnProof(@PathVariable("proof-id") long proofId,
                                        Authentication auth) {
        log.info("@GetMapping(\"/proofs/{proof-id}/kudos\")");
        log.info("Getting proof-id = {}", proofId);
        return kudosService.getKudosOnProof(proofId, auth);
    }


    @Operation(
            summary = "Add kudos on proof",
            description = "Add kudos from Sponsor on proof"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    implementation = Exception.class
                            )
                    )
            )
    })
    @PostMapping("/proofs/{proof-id}/kudos")
    @ResponseStatus(HttpStatus.CREATED)
    public KudosEntity addKudos(@PathVariable("proof-id") long proofId,
                                @RequestParam int kudos,
                                Authentication auth) {
        log.info("@PostMapping(\"/proofs/{proof-id}/kudos\")");
        log.info("Getting proof-id = {}", proofId);
        return kudosService.addKudosOnProof(proofId, kudos, auth);
    }
}*/

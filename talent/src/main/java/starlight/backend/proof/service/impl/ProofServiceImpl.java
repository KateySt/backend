package starlight.backend.proof.service.impl;

/*
@AllArgsConstructor
@Service
@Transactional
@Slf4j
public class ProofServiceImpl implements ProofServiceInterface {
    private final String DATA_CREATED = "dateCreated";
    private ProofRepository repository;
    private TalentRepository talentRepository;
    private ProofMapper mapper;
    private KudosRepository kudosRepository;
    private SkillServiceInterface skillService;
    private TalentServiceInterface talentService;

    @Override
    public ProofPagePagination proofsPagination(int page, int size, boolean sort) {
        var pageRequest = repository.findByStatus(
                Status.PUBLISHED,
                PageRequest.of(page, size, doSort(sort, DATA_CREATED)));
        if (page >= pageRequest.getTotalPages())
            throw new PageNotFoundException(page);
        return mapper.toProofPagePagination(pageRequest);
    }

    @Override
    public ProofPagePaginationWithSkills proofsPaginationWithSkills(int page, int size, boolean sort) {
        var pageRequest = repository.findByStatus(
                Status.PUBLISHED,
                PageRequest.of(page, size, doSort(sort, DATA_CREATED)));
        if (page >= pageRequest.getTotalPages())
            throw new PageNotFoundException(page);
        return mapper.toProofPagePaginationWithSkills(pageRequest);
    }

    @Override
    public ResponseEntity<?> getLocationForAddProofWithSkill(long talentId,
                                                             ProofAddWithSkillsRequest proofAddWithSkillsRequest,
                                                             Authentication auth) {
        if (talentService.checkingLoggedAndToken(talentId, auth)) {
            throw new UserAccesDeniedToProofException();
        }
        long proofId = addProofProfileWithSkill(talentId, proofAddWithSkillsRequest, auth);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{proof-id}")
                .buildAndExpand(proofId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public long addProofProfileWithSkill(long talentId,
                                         ProofAddWithSkillsRequest proofAddWithSkillsRequest,
                                         Authentication auth) {

        var talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new ProofNotFoundException(talentId));
        talent.setTalentSkills(skillService.existsSkill(
                talent.getTalentSkills(),
                proofAddWithSkillsRequest.skills()));
        talentRepository.save(talent);

        var proof = repository.save(ProofEntity.builder()
                .title(proofAddWithSkillsRequest.title())
                .description(proofAddWithSkillsRequest.description())
                .link(proofAddWithSkillsRequest.link())
                .status(Status.DRAFT)
                .dateCreated(Instant.now())
                .talent(talentRepository.findById(talentId)
                        .orElseThrow(() -> new TalentNotFoundException(talentId)))
                .skills(proofAddWithSkillsRequest.skills().stream()
                        .map(skill -> skillService.skillValidation(skill))
                        .filter(Objects::nonNull)
                        .distinct()
                        .toList())
                .build());
        return proof.getProofId();
    }

    @Override
    @Transactional(readOnly = true)
    public ProofPagePagination getTalentAllProofsWithKudoses(Authentication auth, long talentId,
                                                             int page, int size, boolean sort, String status) {
        if (!talentService.checkingLoggedAndToken(talentId, auth)) {
            Page<ProofEntity> pageRequest = getPaginationForTheTalent(talentId, page, size, sort, status);
            return mapper.toProofPagePaginationWithProofFullInfoWithKudoses(pageRequest);
        }
        var pageRequest = getPaginationForTheTalent(talentId, page,
                size, sort, Status.PUBLISHED.name());

        return mapper.toProofPagePaginationWithProofFullInfoWithKudoses(pageRequest);
    }

    @Override
    public ProofEntity addProofProfile(long talentId, ProofAddRequest proofAddRequest) {
        return repository.save(ProofEntity.builder()
                .title(proofAddRequest.title())
                .description(proofAddRequest.description())
                .link(proofAddRequest.link())
                .status(Status.DRAFT)
                .dateCreated(Instant.now())
                .talent(talentRepository.findById(talentId)
                        .orElseThrow(() -> new TalentNotFoundException(talentId)))
                .build());
    }

    @Override
    public ResponseEntity<?> getLocation(long talentId,
                                         ProofAddRequest proofAddRequest,
                                         Authentication auth) {
        if (talentService.checkingLoggedAndToken(talentId, auth)) {
            throw new UserAccesDeniedToProofException();
        }
        long proofId = addProofProfile(talentId, proofAddRequest).getProofId();
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{proof-id}")
                .buildAndExpand(proofId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @Override
    public ProofFullInfo proofUpdateRequest(long talentId, long id, ProofUpdateRequest proofUpdateRequest, Authentication auth) {
        if (talentService.checkingLoggedAndToken(talentId, auth)) {
            throw new UserAccesDeniedToProofException();
        }
        if (!repository.existsByTalent_TalentIdAndProofId(talentId, id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "don`t have that talent");
        }
        var proofEntity = repository.findById(id)
                .orElseThrow(() -> new ProofNotFoundException(id));
        var talent = talentRepository.findById(talentId)
                .orElseThrow(() -> new UserNotFoundException(id));
        if (!proofEntity.getStatus().equals(Status.DRAFT)
                && proofUpdateRequest.status().equals(Status.DRAFT)) {
            throw new UserCanNotEditProofNotInDraftException();
        }
        if (proofEntity.getStatus().equals(Status.DRAFT)) {
            return changeStatusFromDraft(proofUpdateRequest, proofEntity);
        }
        if (proofUpdateRequest.status().equals(Status.HIDDEN)
                || proofUpdateRequest.status().equals(Status.PUBLISHED)) {
            proofEntity.setStatus(proofUpdateRequest.status());
        }
        proofEntity.setSkills(skillService.existsSkill(
                proofEntity.getSkills(),
                proofUpdateRequest.skills()));
        talent.setTalentSkills(skillService.existsSkill(
                proofEntity.getSkills(),
                proofUpdateRequest.skills()));
        talentRepository.save(talent);
        proofEntity.setDateLastUpdated(Instant.now());
        repository.save(proofEntity);
        return mapper.toProofFullInfo(proofEntity);
    }

    private ProofFullInfo changeStatusFromDraft(ProofUpdateRequest proofUpdateRequest, ProofEntity proofEntity) {
        proofEntity.setTitle(validationField(
                proofUpdateRequest.title(),
                proofEntity.getTitle()));
        proofEntity.setDescription(validationField(
                proofUpdateRequest.description(),
                proofEntity.getDescription()));
        proofEntity.setLink(validationField(
                proofUpdateRequest.link(),
                proofEntity.getLink()));
        proofEntity.setStatus(proofUpdateRequest.status());
        proofEntity.setDateLastUpdated(Instant.now());
        repository.save(proofEntity);
        return mapper.toProofFullInfo(proofEntity);
    }

    private String validationField(String newParam, String lastParam) {
        return newParam == null ?
                lastParam :
                newParam;
    }

    @Override
    public void deleteProof(long talentId, long proofId, Authentication auth) {
        if (talentService.checkingLoggedAndToken(talentId, auth)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "you cannot delete proof another talent");
        }
        ProofEntity proof = repository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        proof.setTalent(null);
        for (KudosEntity kudos : kudosRepository.findByProof_ProofId(proofId)) {
            kudos.setProof(null);
            kudos.setOwner(null);
            kudosRepository.deleteById(kudos.getKudosId());
        }
        proof.getKudos().clear();
        repository.deleteById(proofId);
    }

    @Override
    public ProofPagePagination getTalentAllProofs(Authentication auth, long talentId,
                                                  int page, int size, boolean sort, String status) {
        isStatusCorrect(status);
        if (!talentService.checkingLoggedAndToken(talentId, auth)) {
            Page<ProofEntity> pageRequest = getPaginationForTheTalent(talentId, page, size, sort, status);
            return mapper.toProofPagePagination(pageRequest);
        }
        var pageRequest = getPaginationForTheTalent(talentId, page,
                size, sort, Status.PUBLISHED.name());
        return mapper.toProofPagePagination(pageRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ProofPagePaginationWithSkills getTalentAllProofsWithSkills(Authentication auth, long talentId,
                                                                      int page, int size, boolean sort, String status) {
        isStatusCorrect(status);
        if (!talentService.checkingLoggedAndToken(talentId, auth)) {
            Page<ProofEntity> pageRequest = getPaginationForTheTalent(talentId, page, size, sort, status);
            return mapper.toProofPagePaginationWithSkills(pageRequest);
        }
        var pageRequest = getPaginationForTheTalent(talentId, page,
                size, sort, Status.PUBLISHED.name());
        return mapper.toProofPagePaginationWithSkills(pageRequest);
    }

    private Page<ProofEntity> getPaginationForTheTalent(long talentId, int page, int size,
                                                        boolean sort, String status) {
        isStatusCorrect(status);
        return (status.equals(Status.ALL.getStatus())) ?
                repository.findByTalent_TalentId(talentId,
                        PageRequest.of(page, size, doSort(sort, DATA_CREATED)))
                :
                repository.findByTalent_TalentIdAndStatus(talentId, Status.valueOf(status),
                        PageRequest.of(page, size, doSort(sort, DATA_CREATED)));
    }

    @Override
    public ProofFullInfo getProofFullInfo(Authentication auth, long proofId) {
        ProofEntity proof = repository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        var talentId = proof.getTalent().getTalentId();
        if (!talentService.checkingLoggedAndToken(talentId, auth)) {
            return mapper.toProofFullInfo(proof);
        } else if (proof.getStatus().equals(Status.PUBLISHED)) {
            return mapper.toProofFullInfo(proof);
        }
        throw new ProofNotFoundException(proofId);
    }

    @Override
    public ProofFullInfoWithSkills getProofFullInfoWithSkills(Authentication auth, long proofId) {
        ProofEntity proof = repository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        var talentId = proof.getTalent().getTalentId();
        if (!talentService.checkingLoggedAndToken(talentId, auth)) {
            return mapper.toProofFullInfoWithSkills(proof);
        } else if (proof.getStatus().equals(Status.PUBLISHED)) {
            return mapper.toProofFullInfoWithSkills(proof);
        }
        throw new ProofNotFoundException(proofId);
    }

    public Sort doSort(boolean sort, String sortParam) {
        Sort dateSort;
        if (sort) {
            dateSort = Sort.by(sortParam).descending();
        } else {
            dateSort = Sort.by(sortParam);
        }
        return dateSort;
    }

    private void isStatusCorrect(String status) {
        if (!Arrays.toString(Status.values())
                .matches(".*" + Pattern.quote(status) + ".*")) {
            throw new InvalidStatusException(status);
        }
    }
}*/

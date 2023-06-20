package starlight.backend.kudos.service.impl;


/*
@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class KudosServiceImpl implements KudosServiceInterface {
    private KudosRepository kudosRepository;
    private ProofRepository proofRepository;
    private TalentRepository talentRepository;
    private TalentServiceInterface talentService;

    private boolean isProofAlreadyHaveKudosFromUser(long proofId, Authentication auth) {
        var proof = proofRepository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
       var kudosList = proof.getKudos()
                .stream()
                .filter(k -> k.getOwner()
                        .getSponsorId()
                        .toString()
                        .equals(auth.getName()))
                .toList();
        return !kudosList.isEmpty();
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public KudosOnProof getKudosOnProof(long proofId, Authentication auth) {
        var proof = proofRepository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        var kudos = proof.getKudos();
        int countKudos = kudos
                .stream()
                .mapToInt(KudosEntity::getCountKudos)
                .sum();
        log.info("countKudos = {}", countKudos);
        if (auth != null) {
            for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
                if (grantedAuthority.getAuthority().equals(Role.SPONSOR.getAuthority()) &&
                        securityService.isSponsorActive(auth)) {
                    log.info("Is Sponsor = {}", grantedAuthority.getAuthority().equals(Role.SPONSOR.getAuthority()));
                    var kudosFromMeList = kudos.stream()
                            .filter(k -> k.getOwner()
                                    .getSponsorId()
                                    .toString()
                                    .equals(auth.getName()))
                            .toList();
                    int kudosFromMe;
                    if (kudosFromMeList.isEmpty()) {
                        kudosFromMe = 0;
                    } else {
                        kudosFromMe = kudosFromMeList.stream()
                                .mapToInt(KudosEntity::getCountKudos)
                                .sum();
                    }
                    return KudosOnProof.builder()
                            .kudosOnProof(countKudos)
                            .isKudosed(isProofAlreadyHaveKudosFromUser(proofId, auth))
                            .kudosFromMe(kudosFromMe)
                            .build();
                }
            }
        }
        return KudosOnProof.builder()
                .kudosOnProof(countKudos)
                .kudosFromMe(0)
                .isKudosed(false)
                .build();
    }

    @Override
    public KudosEntity addKudosOnProof(long proofId, int kudosRequest, Authentication auth) {
        /*if (tal.isSponsorActive(auth)) {
            throw new YouAreInDeletingProcess();
        }
        if (auth == null) {
            throw new AuthorizationFailureException();
        }
        for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
            if (!grantedAuthority.getAuthority().equals(Role.SPONSOR.getAuthority())) {
                throw new TalentCanNotAddKudos();
            }
        }
        if (kudosRequest == 0) {
            throw new KudosRequestMustBeNotZeroException();
        }
        var proof = proofRepository.findById(proofId)
                .orElseThrow(() -> new ProofNotFoundException(proofId));
        var owner = sponsorRepository.findById(Long.valueOf(auth.getName()))
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));
        if (kudosRequest > owner.getUnusedKudos()) {
            throw new NotEnoughKudosException();
        }
        var follower = talentRepository.findById(proof.getTalent().getTalentId())
                .orElseThrow(() -> new UserNotFoundException(auth.getName()));
        updateSponsorUnusedKudos(owner, kudosRequest);
        return updateSponsorKudosField(proof, follower, owner, kudosRequest, proofId);
        return null;
    }

    private void updateSponsorUnusedKudos(SponsorEntity owner, int kudosRequest) {
        sponsorRepository.findById(owner.getSponsorId()).map(sponsor -> {
            sponsor.setUnusedKudos(owner.getUnusedKudos() - kudosRequest);
            sponsorRepository.save(sponsor);
            return null;
        });
    }

    private KudosEntity updateSponsorKudosField(ProofEntity proof, TalentEntity follower, SponsorEntity owner,
                                                int kudosRequest, long proofId) {
        if (proof.getKudos().stream()
                .filter(kudos1 -> kudos1.getOwner().getSponsorId().equals(owner.getSponsorId()))
                .collect(Collectors.toSet()).isEmpty()) {
            if (kudosRequest < 0) throw new YouCanNotReturnMoreKudosThanGaveException();
            var kudosBuild = KudosEntity.builder()
                    .followerId(follower.getTalentId())
                    .createData(Instant.now())
                    .proof(proof)
                    .owner(owner)
                    .countKudos(kudosRequest)
                    .build();
            kudosRepository.save(kudosBuild);
            return kudosBuild;
        }
        var kudos = kudosRepository.findByOwner_SponsorIdAndProof_ProofId(owner.getSponsorId(), proofId);
        if (kudos.getCountKudos() + kudosRequest < 0) {
            throw new YouCanNotReturnMoreKudosThanGaveException();
        }
        kudos.setCountKudos(kudos.getCountKudos() + kudosRequest);
        kudos.setUpdateData(Instant.now());
        kudosRepository.save(kudos);
        if (kudos.getCountKudos() == 0) {
            kudosRepository.delete(kudos);
        }
        return kudos;
    }
}*/
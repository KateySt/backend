package starlight.backend.talent.service.impl;

/*
@RunWith(SpringRunner.class)
@SpringBootTest
class TalentServiceImplTest {
    @MockBean
    private MapperTalent mapper;
    @MockBean
    private UserRepository repository;
    @MockBean
    private PositionRepository positionRepository;
    @MockBean
    private KudosRepository kudosRepository;
    @MockBean
    private ProofRepository proofRepository;
    @MockBean
    private SecurityServiceInterface securityService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private Authentication auth;
    @Autowired
    private TalentServiceImpl talentService;
    private UserEntity user;

    @BeforeEach
    public void setup() {
        user = UserEntity.builder()
                .userId(1L)
                .fullName("Jon Snow")
                .email("myemail@gmail.com")
                .password("Secret123")
                .build();
    }

    @DisplayName("JUnit test for pagination (talents) method")
    @Test
    void talentPagination_WithValidPageNumber_ShouldGetPage() {
        // Given
        int page = 0;
        int size = 10;
        List<UserEntity> users = Arrays.asList(user, user);
        Page<UserEntity> pageRequest = new PageImpl<>(users);
        TalentPagePagination expectedPagination = TalentPagePagination.builder().build();
        given(repository.findAll(any(Pageable.class))).willReturn(pageRequest);
        given(mapper.toTalentPagePagination(pageRequest)).willReturn(expectedPagination);

        // When
        TalentPagePagination resultPagination = talentService.talentPagination(page, size);

        // Then
        assertThat(resultPagination).isEqualTo(expectedPagination);
    }

    @DisplayName("JUnit test for pagination (talents) method which throw exception")
    @Test
    void talentPagination_WithInvalidPageNumber_ShouldThrowPageNotFoundException() {
        // Given
        int page = 10;
        int size = 10;
        Page<UserEntity> pageRequest = new PageImpl<>(Collections.emptyList());
        given(repository.findAll(any(Pageable.class))).willReturn(pageRequest);

        // When // Then
        assertThatThrownBy(() -> talentService.talentPagination(page, size))
                .isInstanceOf(PageNotFoundException.class)
                .hasMessage("No such page " + page);
    }

    @DisplayName("JUnit test for get full info about talent method")
    @Test
    void talentFullInfo() {
        // Given
        TalentFullInfo expectedFullInfo = TalentFullInfo.builder().build();
        given(repository.findById(user.getUserId())).willReturn(Optional.of(user));
        given(mapper.toTalentFullInfo(user)).willReturn(expectedFullInfo);

        // When
        TalentFullInfo resultFullInfo = talentService.talentFullInfo(user.getUserId());

        // Then
        assertThat(resultFullInfo).isEqualTo(expectedFullInfo);
    }

    @DisplayName("JUnit test for get full info about talent method which throw exception")
    @Test
    void talentFullInfo_WithInvalidId_ShouldThrowTalentNotFoundException() {
        // Given
        given(repository.findById(user.getUserId())).willReturn(Optional.empty());

        // When // Then
        assertThatThrownBy(() -> talentService.talentFullInfo(user.getUserId()))
                .isInstanceOf(TalentNotFoundException.class)
                .hasMessage("Talent not found by id " + user.getUserId());
    }

    @DisplayName("JUnit test for update info about talent method")
    @Test
    void updateTalentProfile() {
        // Given
        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder()
                .fullName("Joly Moth")
                .password("Secret123")
                .birthday(LocalDate.of(1995, 1, 1))
                .avatar("https://example.com/new-avatar.jpg")
                .education("Master's Degree")
                .experience("5 years")
                .positions(List.of("Senior Software Engineer"))
                .build();

        TalentFullInfo updatedTalentInfo = TalentFullInfo.builder().build();
        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(securityService.checkingLoggedAndToken(user.getUserId(), auth)).thenReturn(true);
        when(passwordEncoder.encode(updateRequest.password())).thenReturn(user.getPassword());
        when(mapper.toTalentFullInfo(any())).thenReturn(updatedTalentInfo);
        // When
        TalentFullInfo result = talentService.updateTalentProfile(user.getUserId(), updateRequest, auth);

        // Then
        assertEquals(updatedTalentInfo, result);
        assertEquals(updateRequest.password(), user.getPassword());
        if (updateRequest.fullName() != null) {
            assertEquals(updateRequest.fullName(), user.getFullName());
        } else {
            assertEquals("Jon Snow", user.getFullName());
        }
        if (updateRequest.positions() != null && !updateRequest.positions().isEmpty()) {
            assertEquals(updateRequest.positions(), user.getPositions().stream()
                    .map(PositionEntity::getPosition).collect(Collectors.toList()));
        } else {
            assertEquals(List.of("Senior Software Engineer"), user.getPositions().stream()
                    .map(PositionEntity::getPosition).collect(Collectors.toList()));
        }
        assertEquals(updateRequest.avatar(), user.getAvatar());
        assertEquals(updateRequest.education(), user.getEducation());
        assertEquals(updateRequest.experience(), user.getExperience());

        verify(repository, times(1)).findById(user.getUserId());
    }

    @DisplayName("JUnit test for update info about talent method which throw exception Unauthorized")
    @Test
    void updateTalentProfile_WithInvalidId_ShouldThrowUnauthorizedException() {
        // Given
        TalentUpdateRequest updateRequest = TalentUpdateRequest.builder()
                .fullName("Jon Snow")
                .password("Secret123")
                .avatar("https://example.com/new-avatar.jpg")
                .education("New University")
                .experience("New Experience")
                .positions(Collections.singletonList("Manager"))
                .build();

        when(repository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(updateRequest.password())).thenReturn(user.getPassword());
        when(securityService.checkingLoggedAndToken(user.getUserId(), null)).thenReturn(false);

        // When // Then
        assertThatThrownBy(() -> talentService.updateTalentProfile(user.getUserId(), updateRequest, null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("401 UNAUTHORIZED \"you cannot change another talent\"");
    }

    @DisplayName("Delete talent profile successfully")
    @Test
    void deleteTalentProfile() {
        // Given
        given(repository.findById(user.getUserId())).willReturn(Optional.of(user));
        when(securityService.checkingLoggedAndToken(user.getUserId(), auth)).thenReturn(true);
        Set<KudosEntity> kudosList = new HashSet<>();
        KudosEntity kudos = new KudosEntity();
        kudos.setKudosId(1L);
        kudos.setOwner(user);
        kudos.setProof(new ProofEntity());
        kudosList.add(kudos);
        user.setKudos(kudosList);
        Set<ProofEntity> proofList = new HashSet<>();
        ProofEntity proof = new ProofEntity();
        proof.setProofId(1L);
        proof.setUser(user);
        proof.setKudos(new HashSet<>());
        proofList.add(proof);
        user.setProofs(proofList);
        user.setAuthorities(new HashSet<>(Arrays.asList("TALENT_ROLE")));
        when(kudosRepository.findByOwner_UserId(user.getUserId())).thenReturn(Collections.emptyList());
        when(proofRepository.findByUser_UserId(user.getUserId())).thenReturn(Collections.emptyList());
        doNothing().when(repository).deleteById(user.getUserId());
        doNothing().when(kudosRepository).deleteById(kudos.getKudosId());
        doNothing().when(proofRepository).deleteById(proof.getProofId());

        // When
        talentService.deleteTalentProfile(user.getUserId(), auth);

        // Then
        verify(securityService).checkingLoggedAndToken(user.getUserId(), auth);
        verify(repository, times(1)).findById(user.getUserId());
        verify(kudosRepository, times(1)).findByOwner_UserId(user.getUserId());
        verify(proofRepository, times(1)).findByUser_UserId(user.getUserId());
        assertTrue(user.getProofs().isEmpty());
    }

    @DisplayName("JUnit test for delete talent method which throw exception Unauthorized")
    @Test
    void deleteTalentProfile_WithInvalidId_ShouldThrowUnauthorizedException() {
        // Given
        when(securityService.checkingLoggedAndToken(user.getUserId(), null)).thenReturn(false);

        // When // Then
        assertThatThrownBy(() -> talentService.deleteTalentProfile(user.getUserId(), null))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessage("401 UNAUTHORIZED \"you cannot delete another talent\"");
    }
}
*/
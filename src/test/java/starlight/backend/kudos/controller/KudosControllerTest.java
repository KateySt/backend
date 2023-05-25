package starlight.backend.kudos.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import starlight.backend.kudos.model.entity.KudosEntity;
import starlight.backend.kudos.model.response.KudosOnProof;
import starlight.backend.kudos.service.KudosServiceInterface;

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(KudosController.class)
class KudosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Authentication auth;

    @MockBean
    private KudosServiceInterface kudosService;

    @DisplayName("Test getKudosOnProofShouldReturnKudosOnProof")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    void getKudosOnProofShouldReturnKudosOnProof() throws Exception {
        // Given
        int proofId = 1;
        KudosOnProof kudosOnProof = KudosOnProof.builder()
                .kudosOnProof(1)
                .kudosFromMe(1)
                .isKudosed(true)
                .build();
        given(kudosService.getKudosOnProof(proofId, auth)).willReturn(kudosOnProof);

        // When // Then
        mockMvc.perform(get("/api/v1/proofs/{proof-id}/kudos", proofId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.kudos_on_proof").value(kudosOnProof.kudosOnProof()))
                .andExpect(jsonPath("$.kudos_from_me").value(kudosOnProof.kudosFromMe()))
                .andExpect(jsonPath("$.is_kudosed").value(kudosOnProof.isKudosed()))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("Test addKudosShouldReturnKudosEntity")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    void addKudosShouldReturnKudosEntity() throws Exception {
        // Given
        int proofId = 1;
        int kudos = 20;
        KudosEntity kudosEntity = KudosEntity.builder()
                .kudosId(1L)
                .followerId(1L)
                .countKudos(20)
                .updateData(Instant.MIN)
                .createData(Instant.MAX)
                .build();
        given(kudosService.addKudosOnProof(proofId, kudos, auth)).willReturn(kudosEntity);
        given(kudosService.addKudosOnProof(eq(proofId), eq(kudos), any(Authentication.class)))
                .willReturn(kudosEntity);

        // When // Then
        mockMvc.perform(post("/api/v1/proofs/{proof-id}/kudos", proofId)
                        .param("kudos", String.valueOf(kudos))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.follower_id").value(kudosEntity.getFollowerId()))
                .andExpect(jsonPath("$.count_kudos").value(kudosEntity.getCountKudos()))
                .andExpect(jsonPath("$.update_data").value(kudosEntity.getUpdateData().toString()))
                .andExpect(jsonPath("$.create_data").value(kudosEntity.getCreateData().toString()))
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @DisplayName("Test addKudosShouldReturnBadRequestWhenKudosParameterIsNotProvided")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    void addKudosShouldReturnBadRequestWhenKudosParameterIsNotProvided() throws Exception {
        // Given
        int proofId = 1;
        given(kudosService.getKudosOnProof(proofId, null))
                .willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // When // Then
        mockMvc.perform(post("/api/v1/proofs/{proof-id}/kudos", proofId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }


    @DisplayName("JUnit test for add Kudos Should Return Unauthorized When Kudos Parameter Is Not Authenticated")
    @Test
    @Order(1)
    @WithMockUser(username = "user1", roles = {"SPONSOR"})
    public void addKudosShouldReturnUnauthorizedWhenKudosParameterIsNotProvided() throws Exception {
        //Given
        int proofId = 1;
        when(kudosService.addKudosOnProof(proofId, 0, auth))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        // When // Then
        mockMvc.perform(post("/api/v1/proofs/{proof-id}/kudos", proofId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("Test addKudosShouldReturnUnauthorizedWhenUserIsNotAuthenticated")
    @Test
    void addKudosShouldReturnUnauthorizedWhenUserIsNotAuthenticated() throws Exception {
        // Given
        int proofId = 1;
        given(kudosService.addKudosOnProof(proofId, 1, null))
                .willThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        // When // Then
        mockMvc.perform(post("/api/v1/proofs/{proof-id}/kudos", proofId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("kudos", "1"))
                .andExpect(status().isUnauthorized());
    }
}
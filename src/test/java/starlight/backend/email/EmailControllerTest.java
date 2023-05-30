package starlight.backend.email;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import starlight.backend.email.model.ChangePassword;
import starlight.backend.email.model.Email;
import starlight.backend.email.service.EmailService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = EmailController.class)
class EmailControllerTest {
    @Autowired
    private EmailController emailController;

    @MockBean
    private EmailService emailService;

    @MockBean
    private Authentication auth;

    @Test
    public void testSendMail() {
        Email email = Email.builder()
                .text("This is a test email")
                .subject("Test email")
                .build();
        long sponsorId = 123;

        emailController.sendMail(email, sponsorId, auth);

        verify(emailService, times(1)).sendMail(eq(email), eq(sponsorId), eq(auth));
    }

    @DisplayName("JUnit test for test Forgot Password")
    @Test
    public void testForgotPassword() {
        //Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String email = "test@test.com";

        // When // Then
        emailController.forgotPassword(request, email);

        verify(emailService, times(1)).forgotPassword(eq(request), eq(email));
    }

    @DisplayName("JUnit test for test Recovery Password")
    @Test
    public void testRecoveryPassword() {
        //Given
        String token = "test_token";
        ChangePassword changePassword = ChangePassword.builder()
                .password("new_password")
                .build();

        // When // Then
        emailController.recoveryPassword(token, changePassword);

        verify(emailService, times(1)).recoveryPassword(eq(token), eq(changePassword));
    }

    @DisplayName("JUnit test for test Recovery Account")
    @Test
    public void testRecoveryAccount() throws Exception {
        //Given
        UUID uuid = UUID.randomUUID();
        ResponseEntity<String> expectedResponse = ResponseEntity
                .ok("Account recovered, please sign in again");

        doNothing().when(emailService).recoverySponsorAccount(uuid);

        ResponseEntity<String> actualResponse = emailController.recoveryAccount(uuid.toString());

        // When // Then
        assertEquals(expectedResponse, actualResponse);
        verify(emailService, times(1)).recoverySponsorAccount(eq((uuid)));
    }
}
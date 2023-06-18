package starlight.backend.email.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ChangePassword(
        @NotBlank
        String password
) {
}

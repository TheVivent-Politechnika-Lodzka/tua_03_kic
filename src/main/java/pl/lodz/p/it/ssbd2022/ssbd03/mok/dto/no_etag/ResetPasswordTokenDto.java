package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Password;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordTokenDto {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "server.error.validation.constraints.notNull.token")
    private String token;

    @Password
    private String password;

}

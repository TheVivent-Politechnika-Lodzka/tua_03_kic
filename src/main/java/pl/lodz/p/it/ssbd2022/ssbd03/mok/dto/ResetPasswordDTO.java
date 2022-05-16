package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Password;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {

    @NotNull
    private String token;
    @NotNull
    private String login;
    @Password
    private String password;

}

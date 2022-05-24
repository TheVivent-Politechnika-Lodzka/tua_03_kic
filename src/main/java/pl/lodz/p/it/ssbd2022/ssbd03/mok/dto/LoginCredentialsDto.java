package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Password;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentialsDto {

    @NotNull(message = "server.error.validation.constraints.notNull.login")
    private String login;

    @NotNull(message = "server.error.validation.constraints.notNull.password")
    private String password;

}

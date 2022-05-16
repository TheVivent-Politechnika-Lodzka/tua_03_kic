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
public class CredentialDto {

    @NotNull
    private String login;
    @NotNull
    private String password;

}

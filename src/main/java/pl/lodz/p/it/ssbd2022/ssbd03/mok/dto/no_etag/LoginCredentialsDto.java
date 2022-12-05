package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Password;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginCredentialsDto {

    private static final long serialVersionUID = 1L;

    @Login
    private String login;

    @Password
    private String password;

}

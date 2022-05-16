package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.FirstName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.LastName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Password;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountDto extends AccountDto {

    @Password
    private String password;

    @Login
    private String login;

    @NotNull
    @Email
    private String email;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    @NotNull
    private boolean isActive;

    @NotNull
    private boolean isConfirmed;

}
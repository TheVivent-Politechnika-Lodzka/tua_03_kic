package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.common.TaggedDto;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.FirstName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.LastName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends TaggedDto {

    @Login
    private String login;
    @FirstName
    private String firstName;
    @LastName
    private String lastName;
    @NotNull
    private boolean isActive;
    @NotNull
    private boolean isConfirmed;
    @NotNull
    @Email
    private String email;
}

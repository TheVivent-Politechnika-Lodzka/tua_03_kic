package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.common.TaggedDto;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto extends TaggedDto {

    @NotNull
    private String login;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private boolean isActive;
    @NotNull
    private boolean isConfirmed;
    @NotNull
    @Email
    private String email;
}

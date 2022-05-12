package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    @NotNull
    private String login;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private boolean confirmed;
    @NotNull
    private boolean active;

    private Long version;
}

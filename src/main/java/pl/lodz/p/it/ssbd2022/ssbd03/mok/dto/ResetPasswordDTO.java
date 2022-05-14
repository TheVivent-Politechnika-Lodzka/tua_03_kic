package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDTO {

    @NotNull
    private String token;
    @NotNull
    private String login;
    @NotNull
    private String password;

}

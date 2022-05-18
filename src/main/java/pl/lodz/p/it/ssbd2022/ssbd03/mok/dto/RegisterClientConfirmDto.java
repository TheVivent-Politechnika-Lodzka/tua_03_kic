package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterClientConfirmDto {

    @NotNull
    private String token;

//    @Login
//    private String login;

}

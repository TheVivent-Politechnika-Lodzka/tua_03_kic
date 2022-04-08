package pl.lodz.p.it.ssbd2022.ssbd03.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class CredentialDto {
    @Getter @Setter
    private String login;
    @Getter @Setter
    private String password;

}

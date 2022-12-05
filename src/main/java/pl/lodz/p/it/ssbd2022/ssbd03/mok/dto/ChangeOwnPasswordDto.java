package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Password;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class ChangeOwnPasswordDto implements Taggable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Long version;

    @Password
    private String oldPassword;

    @Password
    private String newPassword;

    private String captcha;
}

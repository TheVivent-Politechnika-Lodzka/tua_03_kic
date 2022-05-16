package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Password;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {
    @NotNull
    private String oldPassword;

    @Password
    private String newPassword;
}

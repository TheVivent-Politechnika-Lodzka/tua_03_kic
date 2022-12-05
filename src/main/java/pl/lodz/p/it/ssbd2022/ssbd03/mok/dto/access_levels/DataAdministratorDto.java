package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class DataAdministratorDto extends AccessLevelDto {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Email
    String contactEmail;
    @PhoneNumber
    String phoneNumber;

}

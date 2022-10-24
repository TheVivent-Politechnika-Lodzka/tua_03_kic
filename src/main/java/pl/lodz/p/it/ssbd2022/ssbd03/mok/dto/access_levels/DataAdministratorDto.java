package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataAdministratorDto extends AccessLevelDto {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Email
    String contactEmail;
    @PhoneNumber
    String phoneNumber;

}

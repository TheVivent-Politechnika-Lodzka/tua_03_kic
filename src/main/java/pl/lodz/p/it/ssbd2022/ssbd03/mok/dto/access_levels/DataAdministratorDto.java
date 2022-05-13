package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataAdministratorDto extends AccessLevelDto {

    String email;
    String phoneNumber;

}

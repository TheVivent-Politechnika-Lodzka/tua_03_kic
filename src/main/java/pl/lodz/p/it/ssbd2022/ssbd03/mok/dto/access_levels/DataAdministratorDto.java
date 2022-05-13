package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;

@Getter @Setter
@NoArgsConstructor
public class DataAdministratorDto extends AccessLevelDto {

    String email;
    String phoneNumber;

    public DataAdministratorDto(String email, String phoneNumber) {
        super(DataAdministrator.LEVEL_NAME);
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}

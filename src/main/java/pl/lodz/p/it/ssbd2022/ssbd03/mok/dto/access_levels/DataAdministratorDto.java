package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataAdministratorDto extends AccessLevelDto {

    String contactEmail;
    String phoneNumber;

    public DataAdministratorDto(DataAdministrator dataAdministrator) {
        super(DataAdministrator.LEVEL_NAME);
        this.contactEmail = dataAdministrator.getContactEmail();
        this.phoneNumber = dataAdministrator.getPhoneNumber();
    }

}

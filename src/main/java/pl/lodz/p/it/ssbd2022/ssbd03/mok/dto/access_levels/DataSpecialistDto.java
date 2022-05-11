package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.Data;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;

@Data
public class DataSpecialistDto extends AccessLevelDto {

    String email;
    String phoneNumber;

    public DataSpecialistDto(DataSpecialist dataSpecialist) {
        super(DataSpecialist.LEVEL_NAME);
        this.email = dataSpecialist.getEmail();
        this.phoneNumber = dataSpecialist.getPhoneNumber();
    }

}

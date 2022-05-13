package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataSpecialistDto extends AccessLevelDto {

    String email;
    String phoneNumber;

    public DataSpecialistDto(DataSpecialist dataSpecialist) {
        super(DataSpecialist.LEVEL_NAME);
        this.email = dataSpecialist.getEmail();
        this.phoneNumber = dataSpecialist.getPhoneNumber();
    }

}
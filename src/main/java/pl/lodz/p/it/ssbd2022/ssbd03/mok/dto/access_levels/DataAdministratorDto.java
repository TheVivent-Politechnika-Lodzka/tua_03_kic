package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataAdministratorDto extends AccessLevelDto {

    String contactEmail;
    String phoneNumber;

}

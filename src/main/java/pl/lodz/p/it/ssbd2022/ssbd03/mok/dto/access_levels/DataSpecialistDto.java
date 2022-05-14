package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataSpecialistDto extends AccessLevelDto {

    String contactEmail;
    String phoneNumber;

}

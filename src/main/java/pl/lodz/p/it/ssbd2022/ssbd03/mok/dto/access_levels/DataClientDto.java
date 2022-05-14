package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataClientDto extends AccessLevelDto{

    String email;
    String phoneNumber;
    String pesel;

}

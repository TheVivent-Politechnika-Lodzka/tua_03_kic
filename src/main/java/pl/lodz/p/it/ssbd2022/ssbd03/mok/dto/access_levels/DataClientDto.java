package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Pesel;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataClientDto extends AccessLevelDto {

    private static final long serialVersionUID = 1L;

    @PhoneNumber
    String phoneNumber;
    @Pesel
    String pesel;

}

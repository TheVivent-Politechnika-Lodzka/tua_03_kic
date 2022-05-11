package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataClientDto extends AccessLevelDto{

    String email;
    String phoneNumber;
    String pesel;

    public DataClientDto(DataClient dataClient) {
        super(DataClient.LEVEL_NAME);
        this.email = dataClient.getEmail();
        this.phoneNumber = dataClient.getPhoneNumber();
        this.pesel = dataClient.getPesel();
    }

}

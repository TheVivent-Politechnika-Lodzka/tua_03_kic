package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto implements Taggable {

    private UUID id;
    private Long version;

    private AccountWithAccessLevelsDto client;
    private AccountWithAccessLevelsDto specialist;
    private ImplantDto implant;
    private Date startDate;
    private Date endDate;
    private int price;
    private String description;
    private Status status;

}

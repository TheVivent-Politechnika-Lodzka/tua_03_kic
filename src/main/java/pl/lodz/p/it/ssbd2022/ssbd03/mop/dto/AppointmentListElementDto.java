package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Description;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentListElementDto {

    private UUID id;

    //TODO wyznaczyć jakie informacje z konta i implantu
    private Account client;

    private Account specialist;

    private Implant implant;

    private Status status;

    private Date startDate;

    @Description
    private String description;
}

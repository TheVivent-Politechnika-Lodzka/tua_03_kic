package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Description;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentListElementDto {

    private static final long serialVersionUID = 1L;

    private UUID id;

    private AccountWithAccessLevelsDto client;

    private AccountWithAccessLevelsDto specialist;

    private ImplantDto implant;

    private Status status;

    private Instant startDate;
    private Instant endDate;

    @Description
    private String description;

    private int price;

    private ImplantDto appointmentImplant;

}

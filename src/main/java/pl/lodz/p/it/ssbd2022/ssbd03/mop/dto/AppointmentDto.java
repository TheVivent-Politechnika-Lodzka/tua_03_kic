package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Price;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDto implements Taggable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "server.error.validation.constraints.notNull.id")
    private UUID id;

    @NotNull(message = "server.error.validation.constraints.notNull.version")
    private Long version;

    @NotNull
    private AccountWithAccessLevelsDto client;

    @NotNull
    private AccountWithAccessLevelsDto specialist;

    @NotNull
    private ImplantDto implant;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant endDate;

    @Price
    private int price;

    @NotNull
    private String description;

    @NotNull
    private Status status;

    private ImplantDto appointmentImplant;

}

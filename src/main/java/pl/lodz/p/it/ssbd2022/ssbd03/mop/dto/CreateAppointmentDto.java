package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentDto {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "server.error.validation.constraints.notNull.createAppointment.specialist")
    private UUID specialistId;

    @NotNull(message = "server.error.validation.constraints.notNull.createAppointment.implant")
    private UUID implantId;

    @NotNull(message = "server.error.validation.constraints.notNull.createAppointment.startDate")
    private Instant startDate;

}

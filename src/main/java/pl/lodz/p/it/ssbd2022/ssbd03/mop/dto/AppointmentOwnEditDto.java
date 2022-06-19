package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Description;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentOwnEditDto implements Taggable {
    @NotNull(message = "server.error.validation.constraints.notNull.id")
    private UUID id;
    @NotNull(message = "server.error.validation.constraints.notNull.version")
    private Long version;
    @Description
    private String description;
    @NotNull(message = "server.error.validation.constraints.notNull.createAppointment.startDate")
    private Instant startDate;
    @NotNull(message = "server.error.validation.constraints.notNull.status")
    private Status status;
}

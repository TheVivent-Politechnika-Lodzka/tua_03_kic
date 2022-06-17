package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

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
    private UUID id;
    private Long version;
    @Description
    private String description;
    private Instant startDate;
    private Status status;
}

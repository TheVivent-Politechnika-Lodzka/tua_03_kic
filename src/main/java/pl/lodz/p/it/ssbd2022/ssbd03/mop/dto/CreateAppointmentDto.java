package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

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

    private String clientLogin;
    private String specialistLogin;
    private UUID implantId;
    private Instant startDate;

}

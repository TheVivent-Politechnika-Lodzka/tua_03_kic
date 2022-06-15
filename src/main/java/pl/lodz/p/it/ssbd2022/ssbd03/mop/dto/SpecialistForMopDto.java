package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistForMopDto {

    private UUID id;

    private String name;

    private String surname;

    private String email;

    private String phoneNumber;

}

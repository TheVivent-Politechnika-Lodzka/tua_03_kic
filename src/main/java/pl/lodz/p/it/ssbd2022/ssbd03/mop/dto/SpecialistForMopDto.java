package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.LastName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Name;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistForMopDto {

    private UUID id;

    @Name
    private String name;

    @LastName
    private String surname;

    @NotNull(message = "server.error.validation.constraints.notNull.email")
    @Email
    private String email;

    @PhoneNumber
    private String phoneNumber;

}
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
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Url;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistForMopDto {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @Name
    private String firstName;

    @LastName
    private String lastName;

    @Url
    private String url;

    @NotNull(message = "server.error.validation.constraints.notNull.email")
    @Email
    private String email;

    @PhoneNumber
    private String phoneNumber;

}

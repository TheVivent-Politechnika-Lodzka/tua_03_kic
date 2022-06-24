package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpecialistDataDto {

    private static final long serialVersionUID = 1L;

    @Email
    String contactEmail;

    @PhoneNumber
    String phoneNumber;

}

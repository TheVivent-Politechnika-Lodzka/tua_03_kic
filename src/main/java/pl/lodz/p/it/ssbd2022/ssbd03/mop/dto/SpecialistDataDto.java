package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.PhoneNumber;

import javax.validation.constraints.Email;


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

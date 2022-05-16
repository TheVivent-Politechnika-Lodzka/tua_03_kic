package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.common.TaggedDto;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.FirstName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.LastName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithAccessLevelsDto extends TaggedDto {

    @Login
    private String login;
    @FirstName
    private String firstName;
    @LastName
    private String lastName;
    @NotNull
    private boolean isActive;
    @NotNull
    private boolean isConfirmed;


    private List<AccessLevelDto> accessLevels = new ArrayList<>();

}

package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;

import java.util.ArrayList;
import java.util.List;

@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountWithAccessLevelsDto {

    @NotNull
    private String login;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    private Long version;

    private List<AccessLevelDto> accessLevels = new ArrayList<>();

}

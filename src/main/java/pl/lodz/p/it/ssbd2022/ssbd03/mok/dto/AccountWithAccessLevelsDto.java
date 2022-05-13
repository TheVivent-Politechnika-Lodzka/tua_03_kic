package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractDto;
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

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AccountWithAccessLevelsDto extends AbstractDto {

    @NotNull
    private String login;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;


    private List<AccessLevelDto> accessLevels = new ArrayList<>();



}

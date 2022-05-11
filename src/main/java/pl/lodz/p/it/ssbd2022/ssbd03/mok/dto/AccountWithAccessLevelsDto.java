package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
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

@Data
@NoArgsConstructor
public class AccountWithAccessLevelsDto {

    @NotNull
    private String login;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private boolean confirmed;
    @NotNull
    private boolean active;

    private Long version;

    private List<AccessLevelDto> accessLevels = new ArrayList<>();

    public AccountWithAccessLevelsDto(Account account) {
        this.login = account.getLogin();
        this.firstName = account.getFirstName();
        this.version = account.getVersion();
        this.confirmed = account.isConfirmed();
        this.active = account.isActive();

        for (AccessLevel accessLevel : account.getAccessLevelCollection()) {
            if (accessLevel instanceof DataClient)
                this.accessLevels.add(new DataClientDto((DataClient) accessLevel));
            if (accessLevel instanceof DataSpecialist)
                this.accessLevels.add(new DataSpecialistDto((DataSpecialist) accessLevel));
            if (accessLevel instanceof DataAdministrator)
                this.accessLevels.add(new DataAdministratorDto((DataAdministrator) accessLevel));
        }
    }


}

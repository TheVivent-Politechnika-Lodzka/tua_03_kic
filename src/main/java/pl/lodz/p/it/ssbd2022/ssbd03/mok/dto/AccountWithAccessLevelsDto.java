package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;

import java.util.List;

@Data
@NoArgsConstructor
public class AccountWithAccessLevelsDto {

    @NotNull
    private String login;
    @NotNull
    private String firstName;
    @NotNull
    private String surname;
    @NotNull
    private String email;

    // na późniejszą wersję (na innym branchu)
    // @NotNull
    private String phoneNumber;

    private String pesel;

    private Long version;

    private List<AccessLevelDto> accessLevels;

    public AccountWithAccessLevelsDto(Account account) {
        this.login = account.getLogin();
        this.firstName = account.getFirstName();
        this.surname = account.getSurname();
        this.email = account.getEmail();
        this.version = account.getVersion();
    }


}

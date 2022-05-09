package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;

@Data
@NoArgsConstructor
public class AccountEditDto {

    @NotNull
    private String firstName;
    @NotNull
    private String surname;

    // na późniejszą wersję (na innym branchu)
    private String email;
    private String phoneNumber;

    private Long version;

    public AccountEditDto(Account account) {
        this.firstName = account.getFirstName();
        this.surname = account.getSurname();
        this.version = account.getVersion();
    }

}

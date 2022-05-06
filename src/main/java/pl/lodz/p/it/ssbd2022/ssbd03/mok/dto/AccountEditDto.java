package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.Account;

@Data
@NoArgsConstructor
public class AccountEditDto {

    @NotNull
    private String firstName;
    @NotNull
    private String surname;
    @NotNull
    private String email;

    private Long version;

    public AccountEditDto(Account account) {
        this.firstName = account.getFirstName();
        this.surname = account.getSurname();
        this.email = account.getEmail();
        this.version = account.getVersion();
    }

}

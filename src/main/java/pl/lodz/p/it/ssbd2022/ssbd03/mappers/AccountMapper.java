package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;

import static pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper.createAccessLevelFromDto;

public class AccountMapper {

    public static Account createAccountFromDto(AccountWithAccessLevelsDto accountDto) {
        Account account = new Account();
        account.setLogin(accountDto.getLogin());
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());
        account.setConfirmed(accountDto.isConfirmed());
        account.setActive(accountDto.isActive());

        for (AccessLevelDto accessLevelDto : accountDto.getAccessLevels()) {
            account.addAccessLevel(createAccessLevelFromDto(accessLevelDto));
        }
        return account;
    }

    public static AccountDto createAccountDtoFromAccount(Account account) {
        return new AccountDto(
                account.getLogin(),
                account.getFirstName(),
                account.getFirstName(),
                account.isConfirmed(),
                account.isActive(),
                account.getVersion()
        );
    }

    public static AccountWithAccessLevelsDto createAccountWithAccessLevelsDtoFromAccount(Account account) {
        return new AccountWithAccessLevelsDto(
                account.getLogin(),
                account.getFirstName(),
                account.getFirstName(),
                account.isConfirmed(),
                account.isActive(),
                account.getVersion(),
                AccessLevelMapper.createListOfAccessLevelDTO(account.getAccessLevelCollection())
        );
    }


}

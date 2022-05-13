package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractDto;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import static pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccessLevelMapper.createAccessLevelFromDto;
@Stateless
public class AccountMapper {

    @Inject
    private HashAlgorithm hashAlgorithm;

    public Account createAccountFromDto(AccountWithAccessLevelsDto accountDto) {
        Account account = new Account();
        account.setLogin(accountDto.getLogin());
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());

        for (AccessLevelDto accessLevelDto : accountDto.getAccessLevels()) {
            account.addAccessLevel(createAccessLevelFromDto(accessLevelDto));
        }
        return account;
    }

    public AbstractDto tagDto(AbstractDto dto, AbstractEntity entity) {
        dto.setTag(hashAlgorithm.generateDtoTag(
                entity.getId(),
                entity.getVersion()
        ));
        return dto;
    }

    public AccountDto createAccountDtoFromAccount(Account account) {
        AccountDto accountDto = new AccountDto(
                account.getLogin(),
                account.getFirstName(),
                account.getFirstName(),
                account.isActive(),
                account.isConfirmed()
        );
        return (AccountDto) tagDto(accountDto, account);
    }

    public AccountWithAccessLevelsDto createAccountWithAccessLevelsDtoFromAccount(Account account) {
        AccountWithAccessLevelsDto accountDto = new AccountWithAccessLevelsDto(
                account.getLogin(),
                account.getFirstName(),
                account.getFirstName(),
                account.isActive(),
                account.isConfirmed(),
                AccessLevelMapper.createListOfAccessLevelDTO(account.getAccessLevelCollection())
        );
        return (AccountWithAccessLevelsDto) tagDto(accountDto, account);
    }


}

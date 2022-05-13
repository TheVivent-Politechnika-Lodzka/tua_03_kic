package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Stateless
public class EntityToDtoMapper {

    @Inject
    private HashAlgorithm hashAlgorithm;


    public AccountDto getAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setLogin(account.getLogin());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setLastName(account.getLastName());
        return accountDto;
    }


}

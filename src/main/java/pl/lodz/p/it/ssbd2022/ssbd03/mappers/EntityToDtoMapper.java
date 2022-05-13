package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountDto;

@Stateless
public class EntityToDtoMapper {

    public AccountDto getAccountDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setLogin(account.getLogin());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setLastName(account.getLastName());
        return accountDto;
    }


}

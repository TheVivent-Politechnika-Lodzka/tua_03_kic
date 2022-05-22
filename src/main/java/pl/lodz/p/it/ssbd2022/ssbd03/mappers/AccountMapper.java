package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.CreateAccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.RegisterClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Stateless
public class AccountMapper {

    @Inject
    private HashAlgorithm hashAlgorithm;

    @Inject
    private AccessLevelMapper accessLevelMapper;

    @Inject
    AbstractEntityMapper abstractEntityMapper;

    /**
     * Metoda mapuje obiekt typu createAccountDto na obiekt typu Account.
     * @param createAccountDto - dto z którego tworzymy konto
     * @return - konto
     */
    // TODO: Dodanie Javadoc
    public Account createAccountfromCreateAccountDto(CreateAccountDto createAccountDto) {
        Account account = new Account();
        account.setLogin(createAccountDto.getLogin());
        account.setFirstName(createAccountDto.getFirstName());
        account.setLastName(createAccountDto.getLastName());
        account.setEmail(createAccountDto.getEmail());
        account.setPassword(hashAlgorithm.generate(createAccountDto.getPassword().toCharArray()));
        account.setActive(false);
        account.setConfirmed(true);
        account.setLanguage(createAccountDto.getLanguage());

        return account;
    }

    // TODO: Dodanie Javadoc
    public Account createAccountfromCreateClientAccountDto(RegisterClientDto registerClientAccountDto) {
        Account account = new Account();
        account.setLogin(registerClientAccountDto.getLogin());
        account.setFirstName(registerClientAccountDto.getFirstName());
        account.setLastName(registerClientAccountDto.getLastName());
        account.setEmail(registerClientAccountDto.getEmail());
        account.setPassword(hashAlgorithm.generate(registerClientAccountDto.getPassword().toCharArray()));
        account.setLanguage(registerClientAccountDto.getLanguage());
        account.setActive(true);
        account.setConfirmed(false);
        DataClient dataClient = new DataClient();
        dataClient.setPesel(registerClientAccountDto.getPesel());
        dataClient.setPhoneNumber(registerClientAccountDto.getPhoneNumber());
        account.addAccessLevel(dataClient);
        return account;
    }

    // TODO: Dodanie Javadoc
    public Account createAccountFromDto(AccountWithAccessLevelsDto accountDto) {
        Account account = new Account();
        account.setLogin(accountDto.getLogin());
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());
        account.setLanguage(accountDto.getLanguage());

        for (AccessLevelDto accessLevelDto : accountDto.getAccessLevels()) {
            account.addAccessLevel(accessLevelMapper.createAccessLevelFromDto(accessLevelDto));
        }

        return account;
    }

    // TODO: Dodanie Javadoc
    public AccountDto createAccountDtoFromAccount(Account account) {
        AccountDto accountDto = new AccountDto(
                account.getLogin(),
                account.getFirstName(),
                account.getLastName(),
                account.isActive(),
                account.isConfirmed(),
                account.getEmail(),
                account.getLanguage()
        );

        return (AccountDto) abstractEntityMapper.map(accountDto, account);
    }

    // TODO: Dodanie Javadoc
    public AccountWithAccessLevelsDto createAccountWithAccessLevelsDtoFromAccount(Account account) {
        AccountWithAccessLevelsDto accountDto = new AccountWithAccessLevelsDto(
                account.getLogin(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.isActive(),
                account.isConfirmed(),
                account.getLanguage(),
                accessLevelMapper.createListOfAccessLevelDTO(account.getAccessLevelCollection())
        );

        return (AccountWithAccessLevelsDto) abstractEntityMapper.map(accountDto, account);
    }

    public List<AccountWithAccessLevelsDto> createListOfAccountWithAccessLevelDTO(Collection<Account> accounts){
        return null == accounts ? null : accounts.stream()
                .filter(Objects::nonNull)
                .map(this::createAccountWithAccessLevelsDtoFromAccount)
                .collect(Collectors.toList());
    }

}

package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.CreateAccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.RegisterClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.SpecialistDataDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.SpecialistForMopDto;
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

    /**
     * Metoda konwertuje obiekt typu createAccountDto na obiekt typu Account.
     *
     * @param createAccountDto Dto, z którego tworzymy konto
     * @return Skonwertowane konto
     */
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


    /**
     * Metoda konwertuje obiekt typu na obiekt typu Account
     *
     * @param registerClientAccountDto Dto, z którego tworzone jest konto klienta
     * @return Konto skonwertowanego obiektu
     */
    public Account createAccountfromRegisterClientDto(RegisterClientDto registerClientAccountDto) {
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

    /**
     * Metoda konwertująca DTO zawierające AccessLevel na jego encję
     *
     * @param accountDto DTO, które będzie konwertowane
     * @return Encja skonwertowanego DTO
     */
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

    /**
     * Metoda konwertująca encję konta na jego DTO (zawierające AccessLevel)
     *
     * @param account Konto, które będzie konwertowane
     * @return DTO skonwertowanego konta
     */
    public AccountWithAccessLevelsDto createAccountWithAccessLevelsDtoFromAccount(Account account) {
        AccountWithAccessLevelsDto accountDto = new AccountWithAccessLevelsDto(
                account.getId(),
                account.getVersion(),
                account.getLogin(),
                account.getFirstName(),
                account.getLastName(),
                account.getEmail(),
                account.isActive(),
                account.isConfirmed(),
                account.getLanguage(),
                null,
                accessLevelMapper.createListOfAccessLevelDTO(account.getAccessLevelCollection())
        );
        return accountDto;

//        return (AccountWithAccessLevelsDto) abstractEntityMapper.dtoFromEntity(accountDto, account);
    }

    /**
     * Metoda pozwalająca skonwertować listę kont (zawierających AccessLevel)
     *
     * @param accounts Lista kont, które będą konwertowane
     * @return Lista zawierająca DTO skonwertowanych kont
     */
    public List<AccountWithAccessLevelsDto> createListOfAccountWithAccessLevelDTO(Collection<Account> accounts) {
        return null == accounts ? null : accounts.stream()
                .filter(Objects::nonNull)
                .map(this::createAccountWithAccessLevelsDtoFromAccount)
                .collect(Collectors.toList());
    }

    /**
     * Metoda mapująca konto na DTO specjalisty dla MOP'a
     *
     * @param account - konto, które będzie skonwertowane (Account)
     * @return - DTO specjalisty dla MOP'a
     */
    public SpecialistForMopDto accountSpecialistListElementDto(Account account) {
        SpecialistDataDto dataspecialist = accessLevelMapper.dataSpecialistListElementDtoList(account.getAccessLevelCollection());
        SpecialistForMopDto specialistForMopDto = new SpecialistForMopDto(
                account.getId(),
                account.getFirstName(),
                account.getLastName(),
                dataspecialist.getContactEmail(),
                dataspecialist.getPhoneNumber()
        );
        return specialistForMopDto;
    }

    /**
     * Metoda pozwalająca na konwersję listy specialistów ( Collection<Account> ) na listę DTO ( List<SpecialistForMopDto> )
     *
     * @param accounts - lista specialistów
     * @return - lista DTO specialistów
     */
    public List<SpecialistForMopDto> accountSpecialistListElementDtoList(Collection<Account> accounts) {
        return null == accounts ? null : accounts.stream()
                .filter(Objects::nonNull)
                .map(this::accountSpecialistListElementDto)
                .collect(Collectors.toList());
    }

}

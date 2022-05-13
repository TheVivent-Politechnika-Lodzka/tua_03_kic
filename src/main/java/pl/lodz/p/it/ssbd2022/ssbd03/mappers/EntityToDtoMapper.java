package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;
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
        accountDto.setTag(hashAlgorithm.generateDtoTag(
                account.getId(),
                account.getVersion()
        ));
        return accountDto;
    }

    public AccountWithAccessLevelsDto getAccountWithAccessLevelsDto(Account account) {
        AccountWithAccessLevelsDto accountDto = new AccountWithAccessLevelsDto();
        accountDto.setLogin(account.getLogin());
        accountDto.setFirstName(account.getFirstName());
        accountDto.setLastName(account.getLastName());
        accountDto.setTag(hashAlgorithm.generateDtoTag(
                account.getId(),
                account.getVersion()
        ));

        for (AccessLevel accessLevel : account.getAccessLevelCollection()) {
            accountDto.getAccessLevels().add(getAccessLevelDto(accessLevel));
        }

        return accountDto;
    }

    public AccessLevelDto getAccessLevelDto(AccessLevel accessLevel) {
        if (accessLevel instanceof DataAdministrator dataAdministrator) {
            DataAdministratorDto dataAdministratorDto = new DataAdministratorDto();
            dataAdministratorDto.setLevel(DataAdministrator.LEVEL_NAME);
            dataAdministratorDto.setEmail(dataAdministrator.getEmail());
            dataAdministratorDto.setPhoneNumber(dataAdministrator.getPhoneNumber());
            return dataAdministratorDto;
        }
        if (accessLevel instanceof DataSpecialist dataSpecialist) {
            DataSpecialistDto dataSpecialistDto = new DataSpecialistDto();
            dataSpecialistDto.setLevel(DataSpecialist.LEVEL_NAME);
            dataSpecialistDto.setEmail(dataSpecialist.getEmail());
            dataSpecialistDto.setPhoneNumber(dataSpecialist.getPhoneNumber());
            return dataSpecialistDto;
        }
        if (accessLevel instanceof DataClient dataClient) {
            DataClientDto dataClientDto = new DataClientDto();
            dataClientDto.setLevel(DataClient.LEVEL_NAME);
            dataClientDto.setEmail(dataClient.getEmail());
            dataClientDto.setPhoneNumber(dataClient.getPhoneNumber());
            dataClientDto.setPesel(dataClient.getPesel());
            return dataClientDto;
        }
        throw new IllegalArgumentException("Unknown access level type");
    }

}

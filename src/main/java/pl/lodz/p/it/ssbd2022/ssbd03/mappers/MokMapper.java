package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;

public class MokMapper {

    public static Account getAccount(AccountWithAccessLevelsDto accountDto) {
        Account account = new Account();
        account.setLogin(accountDto.getLogin());
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());

        for (AccessLevelDto accessLevelDto : accountDto.getAccessLevels()) {
            account.addAccessLevel(getAccessLevel(accessLevelDto));
        }

        return account;
    }

    public static AccessLevel getAccessLevel(AccessLevelDto accessLevelDto) {

        if(accessLevelDto instanceof DataAdministratorDto) {
            DataAdministratorDto dataAdministratorDto = (DataAdministratorDto) accessLevelDto;
            DataAdministrator dataAdministrator = new DataAdministrator();
            dataAdministrator.setEmail(dataAdministratorDto.getEmail());
            dataAdministrator.setPhoneNumber(dataAdministratorDto.getPhoneNumber());
            return dataAdministrator;
        }

        if(accessLevelDto instanceof DataClientDto) {
            DataClientDto dataClientDto = (DataClientDto) accessLevelDto;
            DataClient dataClient = new DataClient();
            dataClient.setEmail(dataClientDto.getEmail());
            dataClient.setPhoneNumber(dataClientDto.getPhoneNumber());
            dataClient.setPesel(dataClientDto.getPesel());
            return dataClient;
        }

        if(accessLevelDto instanceof DataSpecialistDto) {
            DataSpecialistDto dataSpecialistDto = (DataSpecialistDto) accessLevelDto;
            DataSpecialist dataSpecialist = new DataSpecialist();
            dataSpecialist.setEmail(dataSpecialistDto.getEmail());
            dataSpecialist.setPhoneNumber(dataSpecialistDto.getPhoneNumber());
            return dataSpecialist;
        }

        throw new IllegalArgumentException("Unknown access level type");

    }


}

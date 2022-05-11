package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;

public class MokMapper {

    public static Account getAccount(AccountDto accountDto) {
        Account account = new Account();
        account.setLogin(accountDto.getLogin());
        account.setFirstName(accountDto.getFirstName());
        account.setSurname(accountDto.getSurname());
        account.setEmail(accountDto.getEmail());
        account.setPesel(accountDto.getPesel());
        account.setPhoneNumber(accountDto.getPhoneNumber());

        for (AccessLevelDto accessLevelDto : accountDto.getAccessLevels()) {
            account.addAccessLevel(getAccessLevel(accessLevelDto));
        }

        return account;
    }

    public static AccessLevel getAccessLevel(AccessLevelDto accessLevelDto) {

        return switch (accessLevelDto.getLevel()) {
            case DataAdministrator.LEVEL_NAME -> new DataAdministrator();
            case DataClient.LEVEL_NAME -> new DataClient();
            case DataSpecialist.LEVEL_NAME -> new DataSpecialist();
            default ->
                    // TODO: Dać jakiś odpowiedni wyjątek
                    throw new IllegalArgumentException("Unknown access level");
        };


    }


}

package pl.lodz.p.it.ssbd2022.ssbd03.mok.services;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStoreHandler;
import jakarta.ws.rs.ClientErrorException;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataAdministratorDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.DataSpecialistDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.JWTGenerator;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.ArrayList;
import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MOKService {

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private IdentityStoreHandler indentityStoreHandler;

    @Inject
    private JWTGenerator jwtGenerator;

    public String authenticate(Credential credential) {
        CredentialValidationResult result = indentityStoreHandler.validate(credential);
        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return jwtGenerator.createJWT(result);
        }
        throw new ClientErrorException("Invalid username or password", 401);
    }

    public Account findByLogin(String login) {
        return accountFacade.findByLogin(login);
    }

    public void deactivate(String login) {
        Account account = accountFacade.findByLogin(login);
        account.setActive(false);
        accountFacade.edit(account);
    }

    public void activate(String login) {
        Account account = accountFacade.findByLogin(login);
        account.setActive(true);
        accountFacade.edit(account);
    }

    public Account edit(String login, AccountWithAccessLevelsDto accountDto) {
        Account account = this.findByLogin(login);
        account.setFirstName(accountDto.getFirstName());
        account.setLastName(accountDto.getLastName());
        // tu update access levels
        for (AccessLevelDto accessLevelDto : accountDto.getAccessLevels()) {
            for (AccessLevel accessLevel : account.getAccessLevelCollection()) {
                updateAccessLevelData(accessLevel, accessLevelDto);
            }
        }

        accountFacade.edit(account);
        return account;
    }

    public PaginationData findInRange(int page, int limit) {
        PaginationData paginationData = accountFacade.findInRange(page, limit);
        return paginationData;
    }

    // TODO: ZNALEŹĆ LEPSZY SPOSÓB
    private void updateAccessLevelData(AccessLevel accessLevel, AccessLevelDto accessLevelDto) {
        try {
            DataClient dataClient =  (DataClient) accessLevel;
            DataClientDto dataClientDto = (DataClientDto) accessLevelDto;
            dataClient.setPesel(dataClientDto.getPesel());
            dataClient.setEmail(dataClientDto.getEmail());
            dataClient.setPhoneNumber(dataClientDto.getPhoneNumber());
            return;
        }
        catch (ClassCastException e) {

        }
        try {
            DataAdministrator dataAdministrator =  (DataAdministrator) accessLevel;
            DataAdministratorDto dataAdministratorDto = (DataAdministratorDto) accessLevelDto;
            dataAdministrator.setEmail(dataAdministratorDto.getEmail());
            dataAdministrator.setPhoneNumber(dataAdministratorDto.getPhoneNumber());
            return;
        }
        catch (ClassCastException e) {

        }
        try {
            DataSpecialist dataSpecialist =  (DataSpecialist) accessLevel;
            DataSpecialistDto dataSpecialistDto = (DataSpecialistDto) accessLevelDto;
            dataSpecialist.setEmail(dataSpecialistDto.getEmail());
            dataSpecialist.setPhoneNumber(dataSpecialistDto.getPhoneNumber());
            return;
        }
        catch (ClassCastException e) {

        }

    }

}

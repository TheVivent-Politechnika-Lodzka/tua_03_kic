package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

@Local
public interface MOKServiceInterface {

    default String authenticate(String login, String password) {
        throw new MethodNotImplementedException();
    }

    default Account findAccountByLogin(String login) {
        throw new MethodNotImplementedException();
    }

    default Account deactivateAccount(String login, String etag) {
        throw new MethodNotImplementedException();
    }

    default Account activateAccount(String login, String etag) {
        throw new MethodNotImplementedException();
    }

    default Account editAccount(String login, Account account, String etag) {
        throw new MethodNotImplementedException();
    }

    default PaginationData findAllAccounts(int page, int size) {
        throw new MethodNotImplementedException();
    }

    default Account changeAccountPassword(String login, String newPassword, String etag) {
        throw new MethodNotImplementedException();
    }

    default Account changeAccountPassword(String login, String oldPassword, String newPassword, String etag) {
        throw new MethodNotImplementedException();
    }

    default Account registerAccount(Account account) {
        throw new MethodNotImplementedException();
    }

    default Account confirmRegistration(String token) {
        throw new MethodNotImplementedException();
    }

    default Account createAccount(Account account) {
        throw new MethodNotImplementedException();
    }

    default Account addAccessLevelToAccount(String login, AccessLevel accessLevel) {
        throw new MethodNotImplementedException();
    }

    default Account removeAccessLevelFromAccount(String login, String accessLevelName, String accessLevelEtag) {
        throw new MethodNotImplementedException();
    }

    default Account resetPassword(String login) {
        throw new MethodNotImplementedException();
    }

    default Account confirmResetPassword(String login, String password, String token) {
        throw new MethodNotImplementedException();
    }

}


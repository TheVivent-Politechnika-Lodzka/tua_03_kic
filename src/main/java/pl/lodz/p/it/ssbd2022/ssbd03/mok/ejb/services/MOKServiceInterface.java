package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2022.ssbd03.common.ManagerLocalInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

@Local
public interface MOKServiceInterface extends ManagerLocalInterface {

    // TODO: Dodanie Javadoc
    default String authenticate(String login, String password) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account findAccountByLogin(String login) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account deactivateAccount(String login, String etag) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda odblokowująca konto użytkownika, które zostało uprzednio zablokowane przez administratora
     *
     * @param login Login konta, które ma zostać odblokowane
     * @param eTag  Zmienna zawierająca eTag
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account activateAccount(String login, String eTag) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account editAccount(String login, Account account, String etag) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default PaginationData findAllAccounts(int page, int size, String phrase) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account changeAccountPassword(String login, String newPassword, String etag) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zmieniająca hasło dowolnego użytkownika, wywoływana z poziomu servicu.
     * Może ją wykonać tylko konto z poziomem dostępu administratora
     * @param login Login użytkownika, którego hasło będzie zmieniane
     * @param oldPassword Stare haslo użytkownika, które aktualnie znajduję sie w bazie
     * @param newPassword Nowe hasło do logowania, dla użytkownika
     * @param etag Wartość ETag
     * @return Konto użytkownika, któremu zmieniono hasło
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account changeAccountPassword(String login, String oldPassword, String newPassword, String etag) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account registerAccount(Account account) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account confirmRegistration(String token) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account createAccount(Account account) {
        throw new MethodNotImplementedException();
    }


    /**
     * Dodaj poziom dostępu do konta użytkownika
     *
     * @param login       login użytkownika
     * @param accessLevel dodawany poziom dostępu
     * @return zaaktualizowane konto użytkownika
     */
    default Account addAccessLevelToAccount(String login, AccessLevel accessLevel) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account removeAccessLevelFromAccount(String login, String accessLevelName, String accessLevelEtag) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account resetPassword(String login) {
        throw new MethodNotImplementedException();
    }

    // TODO: Dodanie Javadoc
    default Account confirmResetPassword(String login, String password, String token) {
        throw new MethodNotImplementedException();
    }


}


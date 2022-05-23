package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2022.ssbd03.common.ManagerLocalInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;


@Local
public interface MOKServiceInterface extends ManagerLocalInterface {

    // TODO: Dodanie Javadoc
    default String authenticate(String login, String password) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda znajdująca uzytkownika za pomocą loginu.
     *
     * @param login Login konta, które ma zostać znalezione.
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */

    default Account findAccountByLogin(String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda blokująca konto użytkownika.
     *
     * @param login Login konta, które ma zostać zablokowane
     * @param eTag  Zmienna zawierająca eTag blokowanego konta
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account deactivateAccount(String login, String eTag) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda odblokowująca konto użytkownika, które zostało uprzednio zablokowane przez administratora
     *
     * @param login Login konta, które ma zostać odblokowane
     * @param eTag  Zmienna zawierająca eTag
     * @return Zmodyfikowane konto
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account activateAccount(String login, String eTag) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda odblokowująca konto użytkownika, które zostało uprzednio zablokowane przez administratora
     *
     * @param login Login konta, które ma zostać zmodyfikowane
     * @param account modyfikacje do konta
     * @param etag  Zmienna zawierająca eTag
     * @return Zmodyfikowane konto
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account editAccount(String login, Account account, String etag) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zwracająca listę wszystkich kont, która jest stronicowana, od strony servicu.
     * Metoda umożliwia również wyszukiwanie kont po imieniu i/lub nazwisku
     *
     * @param page   Numery strony, która ma być zwrócona (pierwsza strona jest równa 1)
     * @param size   Maksymalna ilość zwróconych kont na stronę
     * @param phrase Ciąg znaków, dla którego jest zwracana lista, która go spełnia
     *               (w tym przypadku ciąg imienia i nazwiska)
     * @return Lista wszystkich kont
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
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

    /**
     * Metoda tworzy konto i zwraca token pozwalający aktywować konto
     * @param account - dane konta
     * @return token, pozwala na aktywacje konta
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default String registerAccount(Account account) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda wyszukuje użytkownika w bazie danych i potwierdza jego konto
     * @param token - token konta, które ma zostać potwierdzone
     * @return potwierdzone konto
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
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

    /**
     * Metoda wywoływana w ramach chęci zresetowania hasła przez użytkownika o podanym loginie
     *
     * @param login Login użytkownika, dla którego zresetowanie hasła będzie możliwe
     * @return Obiekt konta użytkownika, który będzie mógł zresetować hasło
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default ResetPasswordToken resetPassword(String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda pozwalająca potwierdzić chęć zmiany hasła oraz ustawiająca nowe hasło dla użytkownika
     *
     * @param login    Login użytkownika, którego hasło będzie zmieniane
     * @param password Nowe hasło dla konta użytkownika
     * @param token    Token będący metodą weryfikacji czy dany użytkownik może zmienić swoje hasło
     * @return Obiekt konta użytkownika, którego hasło zostało zmienione
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account confirmResetPassword(String login, String password, String token) {
        throw new MethodNotImplementedException();
    }


}


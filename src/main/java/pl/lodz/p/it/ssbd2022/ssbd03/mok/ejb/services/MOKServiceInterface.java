package pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.services;

import javax.ejb.Local;
import pl.lodz.p.it.ssbd2022.ssbd03.common.ServiceLocalInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.AccountConfirmationToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.tokens.ResetPasswordToken;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.AccessLevel;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level.AccessLevelNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.LoginResponseDto;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.UUID;


@Local
public interface MOKServiceInterface extends ServiceLocalInterface {

    /**
     * Metoda uwierzytelnia użytkownika i zwraca token
     *
     * @param login    Login konta, które ma zostać uwierzytelnione
     * @param password Hasło konta, które ma zostać uwierzytelnione
     * @return token użytkownika uwierzytelnionego
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default String authenticate(String login, String password) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda tworząca refreshToken dla konta
     * @param login Login konta, dla którego ma zostać utworzony refreshToken
     * @return RefreshToken
     */
    default String createRefreshToken(String login)  {
        throw new MethodNotImplementedException();
    }


    /**
     * Metoda odświeża accessToken użytkownika
     * @param refreshToken
     * @return JWTStruct z odświeżonym accessToken
     */
    default LoginResponseDto refreshToken(String refreshToken) {
        throw new MethodNotImplementedException();
    }

    /**
     * Wyszukiwanie konta na podstawie loginu
     *
     * @param login Login konta, które ma zostać znalezione
     * @return znalezione konto
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account findAccountByLogin(String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda blokująca konto użytkownika.
     *
     * @param login Login konta, które ma zostać zablokowane
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account deactivateAccount(String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda odblokowująca konto użytkownika, które zostało uprzednio zablokowane przez administratora
     *
     * @param login Login konta, które ma zostać odblokowane
     * @return Zmodyfikowane konto
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account activateAccount(String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda modyfikująca konto użytkownika
     *
     * @param login   Login konta, które ma zostać zmodyfikowane
     * @param account modyfikacje do konta
     * @param etag    Zmienna zawierająca eTag
     * @return Zmodyfikowane konto
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account editAccount(String login, Account account) {
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

    /**
     * Metoda zmieniająca hasło dowolnego wybranego użytkownika, wywoływana z poziomu serwisu.
     * Może ją wykonać tylko konto z poziomem dostępu administratora
     *
     * @param login       Login użytkownika, którego hasło będzie zmieniane
     * @param newPassword Nowe hasło do logowania, dla użytkownika
     * @param etag        Wartość ETag
     * @return Konto użytkownika, któremu zmieniono hasło
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account changeAccountPassword(String login, String newPassword) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda zmieniająca hasło, wywoływana z poziomu serwisu.
     *
     * @param login       Login użytkownika, którego hasło będzie zmieniane
     * @param oldPassword Stare hasło użytkownika, które aktualnie znajduję się w bazie
     * @param newPassword Nowe hasło do logowania, dla użytkownika
     * @param etag        Wartość ETag
     * @return Konto użytkownika, któremu zmieniono hasło
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account changeAccountPassword(String login, String oldPassword, String newPassword) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda tworzy konto i zwraca token pozwalający aktywować konto
     *
     * @param account Dane konta
     * @return Token, pozwala na aktywacje konta
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default AccountConfirmationToken registerAccount(Account account) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda wyszukuje użytkownika w bazie danych i potwierdza jego konto
     *
     * @param token Token konta, które ma zostać potwierdzone
     * @return Potwierdzone konto
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account confirmRegistration(String token) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda tworząca konto
     *
     * @param account Konto użytkownika, które będzie tworzone
     * @return Utworzone konto
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account createAccount(Account account) {
        throw new MethodNotImplementedException();
    }


    /**
     * Dodaj poziom dostępu do konta użytkownika
     *
     * @param login       Login użytkownika
     * @param accessLevel Dodawany poziom dostępu
     * @return Zaaktualizowane konto użytkownika
     */
    default Account addAccessLevelToAccount(String login, AccessLevel accessLevel) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda odłączająca poziom dostępu dla konta, wywołana z poziomu sewrisu.
     *
     * @param login           Login użytkownika, którego poziom dostępu ma zostać odłączony
     * @param accessLevelName Poziom dostępu, który ma zostać odłączony (klient, specjalista bądź administrator)
     * @param accessLevelEtag Wartość ETag
     * @return Konto użytkownika, którego poziom dostępu został odłączony
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     * @throws AccessLevelNotFoundException  w momencie, gdy nie znaleziono w koncie o danym loginie danego poziomu dostępu
     */
    default Account removeAccessLevelFromAccount(String login, String accessLevelName) {
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
     * @param password Nowe hasło dla konta użytkownika
     * @param token    Token będący metodą weryfikacji czy dany użytkownik może zmienić swoje hasło
     * @return Obiekt konta użytkownika, którego hasło zostało zmienione
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    default Account confirmResetPassword(String password, String token) {
        throw new MethodNotImplementedException();
    }


}


package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z istniejącym już w bazie danych koncie
 */
@ApplicationException(rollback = true)
public class AccountAlreadyExistsException extends AppBaseException {

    private static final String LOGIN_ALREADY_EXISTS = "server.error.appBase.accountWithGivenLoginExists";
    private static final String EMAIL_ALREADY_EXISTS = "server.error.appBase.accountWithGivenEmailExists";

    /**
     * Konstruktor, o dostępie prywatnym, potrzebny do budowania wyjątku przy statycznych metodach
     * znajdujących się w klasie AccountAlreadyExistsException
     * @param message Wiadomość zawarta w wyjątku
     * @return wyjatek typu AccountAlreadyExistsException
     */
    private AccountAlreadyExistsException(String message) {
        super(message, Response.Status.CONFLICT);
    }

    /**
     * Metoda statyczna zwracająca wyjątek AccountAlreadyExistsException
     * w przypadku, gdy konto z podanym loginem istnieje w bazie danych
     * @return wyjatek typu AccountAlreadyExistsException
     */
    public static AccountAlreadyExistsException loginExists() {
        return new AccountAlreadyExistsException(LOGIN_ALREADY_EXISTS);
    }

    /**
     * Metoda statyczna zwracająca wyjątek AccountAlreadyExistsException
     * w przypadku, gdy konto z podanym mailem istnieje w bazie danych
     * @return wyjatek typu AccountAlreadyExistsException
     */
    public static AccountAlreadyExistsException emailExists() {
        return new AccountAlreadyExistsException(EMAIL_ALREADY_EXISTS);
    }
}
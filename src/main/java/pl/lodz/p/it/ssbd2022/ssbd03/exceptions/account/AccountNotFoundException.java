package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z nie odnalezieniem konta w bazie danych
 */
@ApplicationException(rollback = true)
public class AccountNotFoundException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String ACCOUNT_NOT_FOUND = "server.error.appBase.accountNotFound";
    private static final String ACCOUNT_NOT_FOUND_BY_LOGIN = "server.error.appBase.accountNotFoundByLogin";
    private static final String ACCOUNT_NOT_FOUND_BY_ID = "server.error.appBase.accountNotFoundById";

    /**
     * Metoda statyczna zwracająca wyjątek AccountNotFoundException
     * w przypadku gdy nie odnaleziono użytkownika z podanym loginem
     *
     * @return wyjatek typu AccountNotFoundException
     */
    public static AccountNotFoundException notFoundByLogin() {
        return new AccountNotFoundException(ACCOUNT_NOT_FOUND_BY_LOGIN);
    }

    /**
     * Metoda statyczna zwracająca wyjątek AccountNotFoundException
     * w przypadku gdy nie odnaleziono użytkownika z podanym id
     *
     * @return wyjatek typu AccountNotFoundException
     */
    public static AccountNotFoundException notFoundById() {
        return new AccountNotFoundException(ACCOUNT_NOT_FOUND_BY_ID);
    }

    /**
     * Konstruktor, o dostępie prywatnym, potrzebny do budowania wyjątku przy statycznych metodach
     * znajdujących się w klasie AccountNotFoundException
     *
     * @param string Wiadomość zawarta w wyjątku
     * @return wyjatek typu AccountNotFoundException
     */
    private AccountNotFoundException(String string) {
        super(string, Response.Status.NOT_FOUND);
    }
}

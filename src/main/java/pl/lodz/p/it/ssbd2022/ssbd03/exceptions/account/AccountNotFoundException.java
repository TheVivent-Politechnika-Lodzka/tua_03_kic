package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z nie odnalezieniem konta w bazie danych
 */
public class AccountNotFoundException extends AppBaseException {

    private static final String ACCOUNT_NOT_FOUND_BY_LOGIN = "server.error.appBase.accountNotFound";

    public AccountNotFoundException() {
        super(ACCOUNT_NOT_FOUND_BY_LOGIN, Response.Status.NOT_FOUND);
    }
}

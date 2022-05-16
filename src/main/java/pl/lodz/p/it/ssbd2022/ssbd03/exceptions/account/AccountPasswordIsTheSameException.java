package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z podaniem tego samego hasła przy jego zmianie
 */
public class AccountPasswordIsTheSameException extends AppBaseException {

    private static final String ACCOUNT_PASSWORD_IS_THE_SAME_EXCEPTION = "server.error.appBase.passwordIsTheSame";

    public AccountPasswordIsTheSameException() {
        super(ACCOUNT_PASSWORD_IS_THE_SAME_EXCEPTION, Response.Status.BAD_REQUEST);
    }
}

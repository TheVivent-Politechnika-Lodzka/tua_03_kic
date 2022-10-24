package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z niepoprawnie podanym hasłem
 */
@ApplicationException(rollback = true)
public class AccountPasswordMatchException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String ACCOUNT_PASSWORD_MATCH_EXCEPTION = "server.error.appBase.accountPasswordMatch";

    public AccountPasswordMatchException() {
        super(ACCOUNT_PASSWORD_MATCH_EXCEPTION, Response.Status.BAD_REQUEST);
    }
}

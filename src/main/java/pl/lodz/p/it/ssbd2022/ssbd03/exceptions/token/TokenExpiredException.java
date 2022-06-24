package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z przedawnieniem tokenu
 */
@ApplicationException(rollback = true)
public class TokenExpiredException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String TOKEN_EXPIERD_EXCEPTION = "server.error.appBase.tokenExpired";

    public TokenExpiredException() {
        super(TOKEN_EXPIERD_EXCEPTION, Response.Status.METHOD_NOT_ALLOWED);
    }
}

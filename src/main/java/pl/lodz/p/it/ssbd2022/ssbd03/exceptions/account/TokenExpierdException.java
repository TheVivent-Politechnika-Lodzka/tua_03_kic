package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

// TODO: Dodanie Javadoc
@ApplicationException(rollback = true)
public class TokenExpierdException extends AppBaseException {

    // TODO: zmiana na kod do internationalizacji
    private static final String TOKEN_EXPIERD_EXCEPTION = "Token expierd !";

    public TokenExpierdException() {
        super(TOKEN_EXPIERD_EXCEPTION, Response.Status.METHOD_NOT_ALLOWED);
    }
}

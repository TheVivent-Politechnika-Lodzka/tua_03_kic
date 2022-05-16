package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

public class TokenExpierdException extends AppBaseException {

    private static final String TOKEN_EXPIERD_EXCEPTION = "Token expierd !";

    public TokenExpierdException() {
        super(TOKEN_EXPIERD_EXCEPTION, Response.Status.METHOD_NOT_ALLOWED);
    }
}

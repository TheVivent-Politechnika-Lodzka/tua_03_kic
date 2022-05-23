package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.token;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

// TODO: Dodanie Javadoc
@ApplicationException(rollback = true)
public class TokenDecodeInvalidException extends AppBaseException {

    private static final String MESSAGE = "server.error.appBase.tokenDecodeInvalid";

    public TokenDecodeInvalidException() {
        super(MESSAGE, Response.Status.BAD_REQUEST);
    }
}

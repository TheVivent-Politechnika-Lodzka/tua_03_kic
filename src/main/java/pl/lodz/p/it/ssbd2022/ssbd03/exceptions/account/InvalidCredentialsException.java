package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

@ApplicationException(rollback = true)
public class InvalidCredentialsException extends AppBaseException {

    private static final String INVALID_CREDENTIALS = "Invalid email or password";

    public InvalidCredentialsException() {
        super(INVALID_CREDENTIALS, Response.Status.UNAUTHORIZED);
    }
}

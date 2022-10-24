package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z istnieniem poziomu dostępu
 */
@ApplicationException(rollback = true)
public class AccessLevelExistsException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "server.error.accessLevelExists";

    public AccessLevelExistsException() {
        super(MESSAGE, Response.Status.CONFLICT);
    }

}

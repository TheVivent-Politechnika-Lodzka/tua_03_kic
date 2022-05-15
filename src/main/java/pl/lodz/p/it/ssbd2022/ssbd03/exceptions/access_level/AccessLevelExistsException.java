package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

public class AccessLevelExistsException extends AppBaseException {

    private static final String MESSAGE = "server.error.access_level.exists";


    public AccessLevelExistsException() {
        super(MESSAGE, Response.Status.CONFLICT);
    }

}

package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek rzucany, gdy specjalista nie ma wolnych godzin w podanym przedziale.
 */
@ApplicationException(rollback = true)
public class SpecialistHasNoTimeException extends AppBaseException {

    private static final String MESSAGE = "server.error.appBase.specialistHasNoTime";

    /**
     * kontruktor wyjątku
     */
    public SpecialistHasNoTimeException() {
        super(MESSAGE, Response.Status.BAD_REQUEST);
    }

}

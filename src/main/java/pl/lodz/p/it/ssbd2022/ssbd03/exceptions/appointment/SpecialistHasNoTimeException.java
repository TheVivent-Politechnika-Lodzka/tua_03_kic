package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek rzucany, gdy specjalista nie ma wolnych godzin w podanym przedziale.
 */
@ApplicationException(rollback = true)
public class SpecialistHasNoTimeException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "server.error.appBase.specialistHasNoTime";

    /**
     * kontruktor wyjątku
     */
    public SpecialistHasNoTimeException() {
        super(MESSAGE, Response.Status.BAD_REQUEST);
    }

}

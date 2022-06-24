package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;


/**
 * Wyjątek rzucany, gdy użytkownik próbuje zarezerwować wizytę na instalację
 * zarchiwizowanego wszczepu.
 */
@ApplicationException(rollback = true)
public class CantInstallArchivedImplant extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String MESSAGE = "server.error.appBase.cantInstallArchivedImplant";

    /**
     * konstruktor wyjątku
     */
    public CantInstallArchivedImplant() {
        super(MESSAGE, Response.Status.BAD_REQUEST);
    }

}

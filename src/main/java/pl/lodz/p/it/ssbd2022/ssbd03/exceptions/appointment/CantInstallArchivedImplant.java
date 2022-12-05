package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;


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

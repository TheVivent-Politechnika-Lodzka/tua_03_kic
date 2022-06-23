package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;
/**
 * Wyjątek rzucany, gdy klient bądź specjalista chcą edytować nie swoją wizytę
 */
@ApplicationException(rollback = true)
public class UserNotPartOfAppointment extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String USER_NOT_PART_OF_APPOINTMENT = "server.error.appBase.userNotPartOfAppointment";

    /**
     * konstruktor wyjątku
     */
    public UserNotPartOfAppointment() {
        super(USER_NOT_PART_OF_APPOINTMENT, Response.Status.BAD_REQUEST);
    }
}

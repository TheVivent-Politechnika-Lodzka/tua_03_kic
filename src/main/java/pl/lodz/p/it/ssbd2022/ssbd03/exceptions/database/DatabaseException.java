package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z bazą danych
 */
@ApplicationException(rollback = true)
public class DatabaseException extends AppBaseException {

    private static final long serialVersionUID = 1L;


    private static final String DATABASE_ERROR = "server.error.appBase.databaseError";

    public DatabaseException() {
        super(DATABASE_ERROR, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public DatabaseException(Throwable cause) {
        super(DATABASE_ERROR, cause, Response.Status.INTERNAL_SERVER_ERROR);
    }
}

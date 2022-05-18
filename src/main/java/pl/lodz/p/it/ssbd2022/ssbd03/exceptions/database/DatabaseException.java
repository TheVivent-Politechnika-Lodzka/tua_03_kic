package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z bazą danych
 */
@ApplicationException(rollback = true)
public class DatabaseException extends AppBaseException {

    private static final String DATABASE_ERROR = "server.error.appBase.databaseError";

    public DatabaseException() {
        super(DATABASE_ERROR, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public DatabaseException(Throwable cause) {
        super(DATABASE_ERROR, cause, Response.Status.INTERNAL_SERVER_ERROR);
    }
}

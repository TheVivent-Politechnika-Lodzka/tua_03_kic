package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

@ApplicationException(rollback = true)
public class InAppOptimisticLockException extends AppBaseException {

    private static final String MESSAGE = "error.optimistic.lock";

    public InAppOptimisticLockException() {
        super(InAppOptimisticLockException.MESSAGE, Response.Status.CONFLICT);
    }

    public InAppOptimisticLockException(Throwable cause) {
        super(InAppOptimisticLockException.MESSAGE, cause, Response.Status.CONFLICT);
    }

}

package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z blokadą optymistyczną
 */
@ApplicationException(rollback = true)
public class InAppOptimisticLockException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String USER_HAS_OLD_ENTITY = "server.error.optimistic.lock";
    private static final String SIGNATURE_NOT_CORRECT = "server.error.optimistic.lock";

    private InAppOptimisticLockException(String message, Response.Status status) {
        super(message, status);
    }

    public static InAppOptimisticLockException userHasOldEntity() {
        return new InAppOptimisticLockException(USER_HAS_OLD_ENTITY, Response.Status.CONFLICT);
    }

    public static InAppOptimisticLockException signatureNotCorrect() {
        return new InAppOptimisticLockException(SIGNATURE_NOT_CORRECT, Response.Status.BAD_REQUEST);
    }
}

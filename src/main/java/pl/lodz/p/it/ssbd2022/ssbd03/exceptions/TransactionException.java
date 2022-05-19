package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błędy związane z tranzakcją
 */
@ApplicationException(rollback = true)
public class TransactionException extends AppBaseException {

    protected final static String ACCESS_DENIED = "error.accessDenied";
    protected final static String TRANSACTION_ROLLBACK = "error.accessDenied";

    public TransactionException(String message, Response.Status status) {
        super(message, status);
    }

    public static TransactionException forbiddenAccess() {
        return new TransactionException(ACCESS_DENIED, Response.Status.FORBIDDEN);
    }

    public static TransactionException transactionRollback() {
        return new TransactionException(TRANSACTION_ROLLBACK, Response.Status.INTERNAL_SERVER_ERROR);
    }
}

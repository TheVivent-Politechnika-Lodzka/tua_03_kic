package pl.lodz.p.it.ssbd2022.ssbd03.exceptions;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z przerwaniem transakcji
 */
@ApplicationException(rollback = true)
public class TransactionException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String TRANSACTION_ROLLBACK = "server.error.appBase.transactionError";

    public TransactionException() {
        super(TRANSACTION_ROLLBACK, Response.Status.INTERNAL_SERVER_ERROR);
    }
}

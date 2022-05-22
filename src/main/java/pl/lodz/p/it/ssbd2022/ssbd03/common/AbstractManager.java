package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.SessionContext;
import jakarta.ejb.TransactionAttribute;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import static jakarta.ejb.TransactionAttributeType.NOT_SUPPORTED;

public abstract class AbstractManager {

    @Resource
    SessionContext sessionContext;

    protected static final Logger LOGGER = Logger.getGlobal();

    private String transactionId;

    private boolean lastTransactionCommited;

    @TransactionAttribute(NOT_SUPPORTED)
    @PermitAll
    public boolean isLastTransactionCommited() {
        return lastTransactionCommited;
    }

    public void afterBegin() {
        transactionId = Long.toString(System.currentTimeMillis())
                + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        LOGGER.log(
                Level.INFO,
                "Transakcja o id={0}, rozpoczęta w: {1}; tożsamość: {2}",
                new Object[]{
                        transactionId,
                        this.getClass().getName(),
                        sessionContext.getCallerPrincipal().getName()
                }
        );
    }

    public void beforeCompletion() {
        LOGGER.log(
                Level.INFO,
                "Transakcja o id={0}, przed zatwierdzeniem w {1}; tożsamość: {2}",
                new Object[]{
                        transactionId,
                        this.getClass().getName(),
                        sessionContext.getCallerPrincipal().getName()
                }
        );
    }

    public void afterCompletion(boolean committed) {
        lastTransactionCommited = committed;
        LOGGER.log(
                Level.INFO,
                "Transakcja o id={0}, zakończona w {1} (status: {2}); tożsamość {3}",
                new Object[]{
                        transactionId,
                        this.getClass().getName(),
                        committed ? "ZATWIERDZONA" : "ODWOŁANA",
                        sessionContext.getCallerPrincipal().getName()
                }
        );
    }
}

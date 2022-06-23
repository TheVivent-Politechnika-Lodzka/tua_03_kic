package pl.lodz.p.it.ssbd2022.ssbd03.common;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class AbstractEndpoint {

    protected <T> T repeat(ServiceLocalInterface service, Supplier<T> method, List<Class> exceptionsToIgnore) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX = false;

        T result = null;
        do {
            try {
                result = method.get();
                commitedTX = service.isLastTransactionCommited();
            } catch (Throwable e) {
                if (!exceptionsToIgnore.contains(e.getClass())) {
                    throw e;
                }
            }
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX || result == null) {
            throw new TransactionException();
        }

        return result;
    }

    protected <T> T repeat(ServiceLocalInterface service, Supplier<T> method) {
        return repeat(service, method, new ArrayList<>());
    }

}

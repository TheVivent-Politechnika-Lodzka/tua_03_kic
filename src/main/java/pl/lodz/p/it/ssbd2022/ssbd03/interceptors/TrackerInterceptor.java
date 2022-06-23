package pl.lodz.p.it.ssbd2022.ssbd03.interceptors;

import jakarta.annotation.Resource;
import jakarta.ejb.SessionContext;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;
import lombok.extern.java.Log;

import java.util.Arrays;

/**
 * Interceptor służący do przechwytywania wywołań metod z komponentów EJB oraz rejestrowania ich w dzienniku zdarzeń
 */
@Log
public class TrackerInterceptor {

    private static final long serialVersionUID = 1L;

    @Resource
    private SessionContext sessionContext;


    /**
     *
     * @param invocationContext Interfejs reprezentujący kontekst wywołania
     * @return Wynik wywołania metody
     * @throws Exception Wyjątek napotkany podczas wywoływania metody
     */
    @AroundInvoke
    public Object traceInvoke(InvocationContext invocationContext) throws Exception {
        StringBuilder message = new StringBuilder("User: ").append(sessionContext.getCallerPrincipal().getName());
        Object resultValue;

        try {
            message.append(" Intercepted method: ").append(invocationContext.getMethod().toString());

            String parameters = invocationContext.getParameters() != null
                    ? String.format(" Parameters values: %s", Arrays.toString(invocationContext.getParameters()))
                    : " none ";
            message.append(parameters);

            resultValue = invocationContext.proceed();

        } catch (Exception e) {
            message.append(String.format(" Threw an exception: %s ", e));
            log.severe(message.toString());
            throw e;
        }

        String result = resultValue != null
                ? String.format(" Returned a value: %s", resultValue)
                : " Returned no value";
        message.append(result);

        log.info(message.toString());
        return resultValue;
    }


}

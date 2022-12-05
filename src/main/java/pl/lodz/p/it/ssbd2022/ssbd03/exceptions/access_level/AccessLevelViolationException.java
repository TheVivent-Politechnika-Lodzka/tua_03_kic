package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.access_level;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z dodawaniem poziomu dostępu konta
 */
@ApplicationException(rollback = true)
public class AccessLevelViolationException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private final static String CLIENT_CANT_BE_SPECIALIST = "server.error.clientCantBeSpecialist";
    private final static String SPECIALIST_CANT_BE_CLIENT = "server.error.specialistCantBeClient";

    /**
     * Konstruktor, o dostępie prywatnym, potrzebny do budowania wyjątku przy statycznych metodach
     * znajdujących się w klasie AccessLevelViolationException
     *
     * @param message Wiadomość zawarta w wyjątku
     * @return wyjatek typu AccessLevelViolationException
     */
    private AccessLevelViolationException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }

    /**
     * Metoda statyczna zwracająca wyjątek AccessLevelViolationException
     * w przypadku dodawaniu poziomu dostępu specjalisty dla klienta
     *
     * @return wyjatek typu AccessLevelViolationException
     */
    public static AccessLevelViolationException clientCantBeSpecialist() {
        return new AccessLevelViolationException(CLIENT_CANT_BE_SPECIALIST);
    }

    /**
     * Metoda statyczna zwracająca wyjątek AccessLevelViolationException
     * w przypadku dodawaniu poziomu dostępu klienta dla specjalisty
     *
     * @return wyjatek typu AccessLevelViolationException
     */
    public static AccessLevelViolationException specialistCantBeClient() {
        return new AccessLevelViolationException(SPECIALIST_CANT_BE_CLIENT);
    }


}

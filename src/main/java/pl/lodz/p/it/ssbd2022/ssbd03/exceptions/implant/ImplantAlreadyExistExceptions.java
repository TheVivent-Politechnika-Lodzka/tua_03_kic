package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z istniejącym już w bazie danych implantem
 */

@ApplicationException(rollback = true)
public class ImplantAlreadyExistExceptions extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String NAME_ALREADY_EXISTS = "server.error.appBase.implantWithGivenNameExists";

    /**
     * Konstruktor, o dostępie prywatnym, potrzebny do budowania wyjątku przy statycznych metodach
     * znajdujących się w klasie ImplantAlreadyExistExceptions
     *
     * @param message Wiadomość zawarta w wyjątku
     * @return wyjatek typu ImplantAlreadyExistExceptions
     */
    private ImplantAlreadyExistExceptions(String message) {
        super(message, Response.Status.CONFLICT);
    }

    /**
     * Metoda statyczna zwracająca wyjątek ImplantAlreadyExistsException
     * w przypadku, gdy implant z podaną nazwą istnieje w bazie danych
     *
     * @return wyjatek typu ImplantAlreadyExistsException
     */
    public static ImplantAlreadyExistExceptions nameExists() {
        return new ImplantAlreadyExistExceptions(NAME_ALREADY_EXISTS);
    }

}

package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z istniejącym już w bazie danych implantem
 */

@ApplicationException(rollback = true)
public class ImplantAlreadyExistExceptions extends AppBaseException {

    private static final String NAME_ALREADY_EXISTS = "server.error.appBase.implantWithGivenNameExists";

    public ImplantAlreadyExistExceptions(String message) {
        super(message, Response.Status.CONFLICT);
    }

    /**
     *  Metoda statyczna zwracająca wyjątek ImplantAlreadyExistsException
     *  w przypadku, gdy implant z podaną nazwą istnieje w bazie danych
     *  @return wyjatek typu ImplantAlreadyExistsException
     */
    public static ImplantAlreadyExistExceptions nameExists() {
        return new ImplantAlreadyExistExceptions(NAME_ALREADY_EXISTS);
    }

}

package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant;

import jakarta.ejb.ApplicationException;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z próba modyfikacji zaarchiwizowanego wszczepu
 */

@ApplicationException(rollback = true)
public class ImplantArchivedExceptions extends AppBaseException {

    private static final String IMPLANT_TO_EDIT_IS_ARCHIVED = "server.error.appBase.implantToEditIsArchived";

    /**
     * Konstruktor, o dostępie prywatnym, potrzebny do budowania wyjątku przy statycznych metodach
     * znajdujących się w klasie ImplantArchivedExceptions
     * @param message Wiadomość zawarta w wyjątku
     * @return wyjatek typu ImplantAlreadyExistExceptions
     */
    private ImplantArchivedExceptions(String message) {
        super(message, Response.Status.CONFLICT);
    }

    /**
     *  Metoda statyczna zwracająca wyjątek ImplantAlreadyExistsException
     *  w przypadku, gdy implant z podaną nazwą istnieje w bazie danych
     *  @return wyjatek typu ImplantAlreadyExistsException
     */
    public static ImplantArchivedExceptions editArchivedImplant() {
        return new ImplantArchivedExceptions(IMPLANT_TO_EDIT_IS_ARCHIVED);
    }

}

package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;

/**
 * Wyjątek reprezentujący błąd związany z próba modyfikacji zaarchiwizowanego wszczepu
 */

@ApplicationException(rollback = true)
public class ImplantArchivedException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String IMPLANT_TO_EDIT_IS_ARCHIVED = "server.error.appBase.implantToEditIsArchived";

    /**
     * Konstruktor, o dostępie prywatnym, potrzebny do budowania wyjątku przy statycznych metodach
     * znajdujących się w klasie ImplantArchivedExceptions
     *
     * @param message Wiadomość zawarta w wyjątku
     * @return wyjatek typu ImplantAlreadyExistExceptions
     */
    private ImplantArchivedException(String message) {
        super(message, Response.Status.CONFLICT);
    }

    /**
     * Metoda statyczna zwracająca wyjątek ImplantAlreadyExistsException
     * w przypadku, gdy implant z podaną nazwą istnieje w bazie danych
     *
     * @return wyjatek typu ImplantAlreadyExistsException
     */
    public static ImplantArchivedException editArchivedImplant() {
        return new ImplantArchivedException(IMPLANT_TO_EDIT_IS_ARCHIVED);
    }

}

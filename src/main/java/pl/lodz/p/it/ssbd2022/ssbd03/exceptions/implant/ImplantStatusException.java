package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant;

import javax.ejb.ApplicationException;
import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

/**
 * Wyjątek reprezentujący błąd związany z archiwizacją implantu
 */
@ApplicationException(rollback = true)
public class ImplantStatusException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String IMPLANT_STATUS_ALREDY_ARCHIVE = "server.error.appBase.implantAlreadyArchived";

    /**
     * Konstruktor odpowiadający za tworzenie wyjątków dotyczących błędów w ramach archiwizacji implantu
     *
     * @param message Wiadomość zawieracjąca treść wyjątku
     */
    private ImplantStatusException(String message) {
        super(message, Response.Status.BAD_REQUEST);
    }

    /**
     * Metoda zwracająca wyjątek w przypadku próby archiwizacji już zarchiwizowanego implantu
     *
     * @return ImplantStatusException, response status 401
     */
    public static ImplantStatusException implantArleadyArchive() {
        return new ImplantStatusException(IMPLANT_STATUS_ALREDY_ARCHIVE);
    }
}

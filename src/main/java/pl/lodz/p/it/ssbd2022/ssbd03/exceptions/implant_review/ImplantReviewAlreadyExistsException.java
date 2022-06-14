package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review;

import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;


/**
 * Wyjątek reprezentujący błąd związany z istniejącą już w bazie danych recenzją implantu
 */
public class ImplantReviewAlreadyExistsException extends AppBaseException {
    private static final String CLIENT_ALREADY_PROVIDED_REVIEW = "server.error.appBase.clientAlreadyProvidedReview";

    public ImplantReviewAlreadyExistsException() {
        super(CLIENT_ALREADY_PROVIDED_REVIEW, Response.Status.CONFLICT);
    }
}

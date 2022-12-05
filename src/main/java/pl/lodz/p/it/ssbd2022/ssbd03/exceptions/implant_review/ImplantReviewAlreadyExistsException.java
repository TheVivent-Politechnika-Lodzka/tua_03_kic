package pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review;

import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.AppBaseException;

import javax.ws.rs.core.Response;


/**
 * Wyjątek reprezentujący błąd związany z istniejącą już w bazie danych recenzją implantu
 */
public class ImplantReviewAlreadyExistsException extends AppBaseException {

    private static final long serialVersionUID = 1L;

    private static final String CLIENT_ALREADY_PROVIDED_REVIEW = "server.error.appBase.clientAlreadyProvidedReview";

    public ImplantReviewAlreadyExistsException() {
        super(CLIENT_ALREADY_PROVIDED_REVIEW, Response.Status.CONFLICT);
    }
}

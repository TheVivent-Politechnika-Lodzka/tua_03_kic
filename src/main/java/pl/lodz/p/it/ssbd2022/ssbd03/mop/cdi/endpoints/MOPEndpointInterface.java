package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentEditDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantDto;

import java.util.UUID;

import pl.lodz.p.it.ssbd2022.ssbd03.validation.DurationValue;

@DenyAll
public interface MOPEndpointInterface {

    /**
     * MOP.1 - Dodaj nowy wszczep
     *
     * @param createImplantDto - dane nowego wszczepu
     * @return Response - zawierająca status HTTP
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @PUT
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/implant/create")
    default Response createImplant(CreateImplantDto createImplantDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.2 - Archiwizuj wszczep
     *
     * @param id - id wszczepu
     * @return - wszczep i odpowiedz HTTP
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    // MOP.2 - Archiwizuj wszczep
    @PATCH
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/implant/archive/{id}")
    default Response archiveImplant(@PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.3 - Edytuj wszczep
     *
     * @param id         uuid wszczepu do edycji
     * @param implantDto dane do modyfikacji implantu
     * @return Response - zawierająca status HTTP
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @PUT
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/implant/edit/{id}")
    default Response editImplant(@PathParam("id") UUID id, @Valid ImplantDto implantDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.4 - Przeglądaj szczegoły wszczepu
     *
     * @param id uuid wybranego wszczepu
     * @return wszczep
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @GET
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/implant/details/{id}")
    default Response getImplant(@PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.5 - Przeglądaj listę wszczepów
     *
     * @param page     numer strony
     * @param size     ilość pozycji na stronie
     * @param phrase   szukana fraza
     * @param archived określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @GET
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/implant/list")
    default Response listImplants(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("phrase") @DefaultValue("") String phrase, @QueryParam("archived") @DefaultValue("false") boolean archived) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.6 - Przeglądaj listę specialistów
     * dostęp dla wszytskich użytkowników serwisu, włącznie z użytkownikami nieuwierzytelnionymi
     *
     * @param page   - numer strony (int)
     * @param size   - ilość pozycji na stronie (int)
     * @param phrase - szukana fraza (String)
     * @return lista specialistów
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @GET
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/specialist/list")
    default Response listSpecialists(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("phrase") @DefaultValue("") String phrase) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.7 - Przeglądaj listę wizyt
     *
     * @param page   numer aktualnie przeglądanej strony
     * @param size   ilość rekordów na danej stronie
     * @param phrase wyszukiwana fraza
     * @return lista wizyt
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    @GET
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/list/visits")
    default Response listAppointments(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("phrase") @DefaultValue("") String phrase) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.8 - przeglądaj swoje wizyty
     *
     * @param page - strona
     * @param size - ilośc elementów na stronie
     * @return - lista wizyt wraz z odpowiedzią HTTP
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    // MOP.8 - przeglądaj swoje wizyty
    @GET
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/list/visits/my")
    default Response listMyAppointments(@QueryParam("page") int page, @QueryParam("size") int size) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.9 - Zarezerwuj wizytę
     *
     * @param dto - dane nowej wizyty
     * @return status HTTP i utworzoną wizytę
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    @POST // ze względu na dodatkowe akcje zawierające się na utworzenie wizyty (obliczenie ceny, daty końcowej, itp.)
    @RolesAllowed(Roles.CLIENT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/visit/create")
    default Response createAppointment(@Valid CreateAppointmentDto dto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.9 - Zarezerwuj wizytę, dostępność specjalisty
     *
     * @param specialistId - id specjalisty
     * @param month         - miesiąc wizyty (Instant)
     * @param duration      - długość wizyty
     * @return lista dostępności
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    @GET
    @RolesAllowed(Roles.CLIENT)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/specialists/{id}/availability/{month}/{duration}")
    default Response getSpecialistAvailability(
            @PathParam("id") UUID specialistId,
            @PathParam("month") String month,
            @DurationValue @PathParam("duration") int duration) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.10- Edytuj swoją wizytę
     *
     * @param id                    id konkretnej wizyty
     * @param appointmentOwnEditDto obiekt dto edytowanej wizyty
     * @return odpowiedź serwera (wizyta)
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    @PUT
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/edit/visit/my/{id}")
    default Response editOwnAppointment(@PathParam("id") UUID id, @Valid AppointmentOwnEditDto appointmentOwnEditDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.11 - Edytuj dowolną wizytę
     *
     * @param id                 id konkretnej wizyty
     * @param appointmentEditDto obiekt dto edytowanej wizyty
     * @return odpowiedź serwera (wizyta)
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    @PUT
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/edit/visit/{id}")
    default Response editAppointment(@PathParam("id") UUID id, AppointmentEditDto appointmentEditDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.12 - Odwołaj swoją wizytę
     * Endpoint pozwalający odwołać wizytę (REJECTED)
     *
     * @param id - id wizyty
     * @return status HTTP oraz zmodyfikowana wizyta
     * @throws MethodNotImplementedException, gdy metoda nie jest zaimplementowana
     */
    @PATCH
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/visit/cancel/my/{id}")
    default Response cancelOwnAppointment(@PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.13 Odwołaj dowolną wizytę
     * Metodę może wykonać tylko konto z poziomem dostępu administratora.
     *
     * @param id Identyfikator wizyty, która ma zostać odwołana
     * @return odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @DELETE
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/cancel/visit/{id}")
    default Response cancelAnyAppointment(@PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.14 - Oznacz wizytę jako zakończoną
     *
     * @param id identyfikator wizyty, która ma zostać oznaczona jako zakończona
     * @return Response - odpowiedź zawierająca status HTTP
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    @PATCH
    @RolesAllowed(Roles.SPECIALIST)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/finish/visit/{id}")
    default Response finishAppointment(@PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.15 - Dodaj recenzję wszczepu
     * Metodę można wykonać tylko konto z poziomem dostępu klienta.
     *
     * @param createImplantReviewDto Recenzja wszczepu napisana przez użytkownika
     * @return recenzja wszczepu
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @PUT
    @RolesAllowed(Roles.CLIENT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/implant/review")
    default Response addImplantsReview(CreateImplantReviewDto createImplantReviewDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.16 - Usuń recenzję wszczepu
     *
     * @param id Id recenzji wszczepu
     * @return Komunikat o usuniętej recenzji
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @DELETE
    @RolesAllowed({Roles.ADMINISTRATOR, Roles.CLIENT})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/implant/review/{id}")
    default Response deleteImplantsReview(@PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.17 - Pobierz szczegóły wizyty
     *
     * @param uuid Id wizyty
     * @return Szczegóły wizyty
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @GET
    @RolesAllowed(Roles.AUTHENTICATED)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/visit/{id}")
    default Response getAppointmentDetails(@PathParam("id") UUID uuid) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.18 - Wyświetl recenzje dla danego wszczepu
     *
     * @param size Ilość recenzji do wyświetlenia na jednej stronie
     * @param page Numer strony
     * @param id   Identyfikator wszczepu
     * @return Lista recenzji wszczepu
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @GET
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/implant/{id}/reviews")
    default Response getAllImplantReviews(@QueryParam("page") int page, @QueryParam("size") int size, @PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

}

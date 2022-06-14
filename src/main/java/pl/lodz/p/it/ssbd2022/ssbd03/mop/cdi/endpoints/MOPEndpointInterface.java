package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.*;

import java.util.List;
import java.util.UUID;

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
    @Path("/implant/create")
    default Response createImplant(CreateImplantDto createImplantDto) {
        throw new MethodNotImplementedException();
    }

    // MOP.2 - Usuń wszczep (w sumie nwm czy będziemy usuwać, czy tylko archiwizować)
    @DELETE
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete/{id}")
    default Response delete(@PathParam("id") String id) {
        throw new MethodNotImplementedException();
    }

    // MOP.3 - Edytuj wszczep
    @PUT
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/edit")
    default Response edit(String json) {
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
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/implants/details/{id}")
    default Response getImplant(@PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.5 - Przeglądaj listę wszczepów
     * @param page numer strony
     * @param size ilość pozycji na stronie
     * @param phrase szukana fraza
     * @param archived określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @GET
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/implant/list")
    default Response listImplants(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("phrase") @DefaultValue("") String phrase, @QueryParam("archived") @DefaultValue("false") boolean archived) {
        throw new MethodNotImplementedException();
    }

    // MOP.6 - przeglądaj listę specjalistów
    @GET
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/specialists")
    default Response listSpecialists(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("phrase") @DefaultValue("") String phrase) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.7 - Przeglądaj listę wizyt
     *
     * @param page numer aktualnie przeglądanej strony
     * @param size ilość rekordów na danej stronie
     * @param phrase wyszukiwana fraza
     * @return lista wizyt
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    @GET
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/visits")
    default Response listVisits(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("phrase") @DefaultValue("") String phrase) {
        throw new MethodNotImplementedException();
    }

    // MOP.8 - przeglądaj swoje wizyty
    @GET
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/visits/my")
    default Response listMyVisits(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("phrase") @DefaultValue("") String phrase) {
        throw new MethodNotImplementedException();
    }

    // MOP.9 - Zarezerwuj wizytę

    /**
     * MOP.9 - Zarezerwuj wizytę
     * @param dto - dane nowej wizyty
     * @return Response - zawierająca status HTTP i utworzoną wizytę
     */
    @POST // ze względu na dodatkowe akcje zawierające się na utworzenie wizyty (obliczenie ceny, daty końcowej, itp.)
    @RolesAllowed(Roles.CLIENT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/visit/create")
    default Response createAppointment(@Valid CreateAppointmentDto dto) {
        throw new MethodNotImplementedException();
    }

    // MOP.10 - Edytuj swoją wizytę
    @PUT
    @RolesAllowed(Roles.CLIENT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/edit/visit")
    default Response editVisit(String json) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.11 - Edytuj dowolną wizytę
     *
     * @param id id konkretnej wizyty
     * @param appointmentEditDto obiekt dto edytowanej wizyty
     * @return odpowiedź serwera (wizyta)
     * @throws MethodNotImplementedException w przypadku braku implementacji metody
     */
    @PUT
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/edit/visit/{id}")
    default Response editVisit(@PathParam("id") UUID id, AppointmentEditDto appointmentEditDto) {
        throw new MethodNotImplementedException();
    }

    // MOP.12 - Odwołaj swoją wizytę
    @DELETE
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/cancel/visit")
    default Response cancelVisit(String json) {
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
    @Path("/cancel/visit/{id}")
    default Response cancelAnyVisit(@PathParam("id") UUID id) {
        throw new MethodNotImplementedException();
    }

    // MOP.14 - Oznacz wizytę jako zakończoną
    @POST
    @RolesAllowed(Roles.SPECIALIST)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/finish/visit")
    default Response finishVisit(String json) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.15 - Dodaj recenzję wszczepu
     * @param createImplantReviewDto Recenzja wszczepu napisana przez użytkownika
     * @return recenzja wszczepu
     * @throws MethodNotImplementedException - w przypadku braku implementacji metody
     */
    @PUT
    @RolesAllowed(Roles.CLIENT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/implants/reviews")
    default Response addImplantsReview(CreateImplantReviewDto createImplantReviewDto) {
        throw new MethodNotImplementedException();
    }

    // MOP.16 - Usuń recenzję wszczepu
    @DELETE
    @RolesAllowed({Roles.CLIENT, Roles.ADMINISTRATOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/implants/reviews")
    default Response deleteImplantsReview(String json) {
        throw new MethodNotImplementedException();
    }

}

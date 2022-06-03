package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;

@DenyAll
public interface MOPEndpointInterface {

    // MOP.1 - Dodaj wszczep
    @PUT
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/create")
    default Response create(String json) {
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

    // MOP.4 - Pobierz szczegóły wszczepu
    @GET
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/details/{id}")
    default Response details(@PathParam("id") String id) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOP.5 - Przeglądaj listę wszczepów
     * @param page numer strony
     * @param size ilość pozycji na stronie na stronie
     * @param phrase szukana fraza
     * @return lista wszczepów
     */
    @GET
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/implants")
    default Response listImplants(@QueryParam("page") int page, @QueryParam("size") int size, @QueryParam("phrase") @DefaultValue("") String phrase) {
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

    // MOP.7 - przeglądaj wszystkie wizyty
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
    @POST
    @RolesAllowed(Roles.CLIENT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/reserve")
    default Response reserve(String json) {
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

    // MOP.11 - Edytuj dowolną wizytę
    @PUT
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/edit/visit/{id}")
    default Response editVisit(@PathParam("id") String id, String json) {
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

    // MOP.13 - Odwołaj dowolną wizytę
    @DELETE
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/cancel/visit/{id}")
    default Response cancelVisit(@PathParam("id") String id, String json) {
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

    // MOP.15 - Dodaj recenzję wszczepu
    @POST
    @RolesAllowed(Roles.CLIENT)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/add/implants/review")
    default Response addImplantsReview(String json) {
        throw new MethodNotImplementedException();
    }

    // MOP.16 - Usuń recenzję wszczepu
    @DELETE
    @RolesAllowed({Roles.CLIENT, Roles.ADMINISTRATOR})
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/delete/implants/review")
    default Response deleteImplantsReview(String json) {
        throw new MethodNotImplementedException();
    }

}

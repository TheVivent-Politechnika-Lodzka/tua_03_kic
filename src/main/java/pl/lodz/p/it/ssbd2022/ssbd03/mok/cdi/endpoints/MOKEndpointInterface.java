package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.AccessLevelDeserializer;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;


public interface MOKEndpointInterface {

    // ping
    @GET
    @Path("/ping")
    @PermitAll
    default Response ping() {
        return Response.ok("pong").build();
    }


    // MOK.1 Zarejestruj
    // TODO: Dodanie Javadoc
    @POST
    @Path("/register")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response register(@Valid RegisterClientDto registerClientDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.1 Zarejestruj
    // TODO: Dodanie Javadoc
    @POST
    @Path("/register-confirm")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response registerConfirm(@Valid RegisterClientConfirmDto registerConfirmDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.2 Utwórz konto
    // TODO: Dodanie Javadoc
    @PUT
    @Path("/create")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response createAccount(@Valid CreateAccountDto createAccountDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.3 Zablokuj konto
    // TODO: Dodanie Javadoc
    @PATCH
    @Path("/deactivate/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response deactivateAccount(@PathParam("login") String login, @Valid ETagDto eTagDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.4 Odblokuj konto
    // TODO: Dodanie Javadoc
    @PATCH
    @Path("/activate/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response activateAccount(@PathParam("login") String login, @Valid ETagDto eTagDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.5 Dołącz poziom dostępu do konta
    // TODO: Dodanie Javadoc
    @PUT
    @Path("/access-level/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response addAccessLevel(@PathParam("login") String login, @Valid AccessLevelDto accessLevelDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.6 Odłącz poziom dostępu z konta
    // TODO: Dodanie Javadoc
    @DELETE
    @Path("/access-level/{login}/{accessLevel}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    default Response removeAccessLevel(@PathParam("login") String login, @PathParam("accessLevel") String accessLevel, @QueryParam("eTag") String eTag) {
        throw new MethodNotImplementedException();
    }

    // MOK.7 Zmień własne hasło
    // TODO: Dodanie Javadoc
    @PATCH
    @Path("/password")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response changeOwnPassword(@Valid ChangeOwnPasswordDto changeOwnPasswordDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.8 Zmień hasło innego użytkownika
    // TODO: Dodanie Javadoc
    @PATCH
    @Path("/password/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response changePassword(@PathParam("login") String login, @Valid ChangePasswordDto changePasswordDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.9 Edytuj dane własnego konta
    // TODO: Dodanie Javadoc
    @PUT
    @Path("/")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response editOwnAccount(@Valid AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.10 Edytuj dane konta innego użytkownika
    // TODO: Dodanie Javadoc
    @PUT
    @Path("/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response editAccount(@PathParam("login") String login, @Valid AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.11 Wyloguj
    // nie dotyczy backendu

    // MOK.12 Zaloguj
    // TODO: Dodanie Javadoc
    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response login(@Valid LoginCredentialsDto loginCredentialsDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.13 przeglądaj listę wszystkich kont
    // TODO: Dodanie Javadoc
    @GET
    @Path("/list")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAllAccounts(@QueryParam("page") int page, @QueryParam("limit") int limit) {
        throw new MethodNotImplementedException();
    }

    // MOK.14 Zresetuj hasło
    // TODO: Dodanie Javadoc
    @POST
    @Path("/reset-password/{login}")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response resetPassword(@PathParam("login") String login) {
        throw new MethodNotImplementedException();
    }

    // MOK.14 Zresetuj hasło
    // TODO: Dodanie Javadoc
    @POST
    @Path("/reset-password-token")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response resetPasswordToken(@Valid ResetPasswordTokenDto resetPasswordDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.15 Przeglądaj szczegóły własnego konta
    // TODO: Dodanie Javadoc
    @GET
    @Path("/")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    default Response getOwnAccount() {
        throw new MethodNotImplementedException();
    }

    // MOK.16 Przeglądaj szczegóły konta innego użytkownika
    // TODO: Dodanie Javadoc
    @GET
    @Path("/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAccount(@PathParam("login") String login) {
        throw new MethodNotImplementedException();
    }

}

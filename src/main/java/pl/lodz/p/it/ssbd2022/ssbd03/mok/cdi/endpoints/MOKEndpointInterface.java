package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
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

    /**
     * MOK.1 Zarejestruj
     * @param registerClientDto - dane konta
     * @return Response zawierający status HTTP
     * @throws MethodNotImplementedException jeśli metoda nie została zaimplementowana
     */
    @POST
    @Path("/register")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response register(@Valid RegisterClientDto registerClientDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.1 Zarejestruj - potwierdzenie rejestracji konta
     * @param registerConfirmDto token
     * @return Response zawierający status HTTP
     * @throws MethodNotImplementedException jeśli metoda nie została zaimplementowana
     */
    @POST
    @Path("/register-confirm")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response confirmRegistration(@Valid RegisterClientConfirmDto registerConfirmDto) {
        throw new MethodNotImplementedException();
    }

    /**\
     * MOK.2 Utwórz konto
     * @param createAccountDto dane konta
     * @return Response zawierający status HTTP
     * @throws MethodNotImplementedException jeśli metoda nie została zaimplementowana
     */
    @PUT
    @Path("/create")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response createAccount(@Valid CreateAccountDto createAccountDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.3 Zablokuj konto
    /**
     * Metoda blokouje konto użytkownika.
     *
     * @param login   Login konta, które ma zostać zablokowane
     * @param eTagDto Obiekt DTO, zawierający w sobie eTag blokowanego konta
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PATCH
    @Path("/deactivate/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response deactivateAccount(@PathParam("login") String login, @Valid ETagDto eTagDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.4 Odblokuj konto
    /**
     * Metoda odblokowująca konto użytkownika, które zostało uprzednio zablokowane przez administratora
     *
     * @param login   Login konta, które ma zostać odblokowane
     * @param eTagDto Obiekt DTO, zawierający w sobie eTag
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PATCH
    @Path("/activate/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response activateAccount(@PathParam("login") String login, @Valid ETagDto eTagDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.5 Dołącz poziom dostępu do konta
    /**
     * Metoda dodająca poziom dostępu do konta użytkownika
     *
     * @param login   Login konta, które ma zostać odblokowane
     * @param accessLevelDto Obiekt DTO, zawierający informacje o dołączanym poziomie dostępu
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PUT
    @Path("/access-level/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    default Response addAccessLevel(@PathParam("login") String login, @Valid AccessLevelDto accessLevelDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda odłączająca poziom dostępu dla konta, wywołana z poziomu endpointa.
     * Może ją tylko wykonać tylko konto z poziomem dostępu administratora.
     * @param login Login użytkownika, którego poziom dostępu ma zostać odłączony
     * @param accessLevel Poziom dostępu, który ma zostać odłączony (klient, specjalista bądź administrator)
     * @param eTag Wartość ETag
     * @return odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     * @throws TransactionException w momencie, gdy transakcja nie została zatwierdzona
     */
    @DELETE
    @Path("/access-level/{login}/{accessLevel}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    default Response removeAccessLevel(@PathParam("login") String login, @PathParam("accessLevel") String accessLevel, @QueryParam("eTag") String eTag) {
        throw new MethodNotImplementedException();
    }

    // MOK.7 Zmień własne hasło
    /**
     * Metoda zmieniająca hasło aktualnego użytkownika, wywoływana z poziomu endpointa.
     * Metoda dostepna dla kont z dowolnym poziomem dostepu.
     * @param changeOwnPasswordDto Obiekt Dto zawierający etag, stare (aktualne) hasło oraz nowe hasło
     * @return odpowiedź HHTP
     */
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
    /**
     * Metoda pozwalająca na modyfikację danych
     * @param accountWithAccessLevelsDto Obiekt DTO, zawierający informacje o koncie użytkownika
     * @return odpowiedź HTTP, powinna zawierać zmodyfikowane dane użytkownika
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
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

    /**
     * MOK.12 Zaloguj
     * @param loginCredentialsDto - dane logowania
     * @return Response zawierający status HTTP
     * @throws MethodNotImplementedException jeśli metoda nie została zaimplementowana
     */
    @POST
    @Path("/login")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response login(@Valid LoginCredentialsDto loginCredentialsDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.13 przeglądaj listę wszystkich kont

    /**
     * Metoda zwracająca listę wszystkich kont, która jest stronicowana, od strony endopointa.
     * Metoda umożliwia również wyszukiwanie kont po imieniu i/lub nazwisku
     *
     * @param page   Numery strony, która ma być zwrócona (pierwsza strona jest równa 1)
     * @param limit  Maksymalna ilość zwróconych kont na stronę
     * @param phrase Ciąg znaków, dla którego jest zwracana lista, która go spełnia
     *               (w tym przypadku ciąg imienia i nazwiska)
     * @return Lista wszystkich kont
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @GET
    @Path("/list")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAllAccounts(@QueryParam("page") int page, @QueryParam("limit") int limit,
                                    @QueryParam("phrase") @DefaultValue("") String phrase) {
        throw new MethodNotImplementedException();
    }

    // MOK.14 Zresetuj hasło
    /**
     * Metoda umożliwiająca zresetowanie hasła do konta przez użytkownika o zadanym loginie
     *
     * @param login Login użytkownika, który wyraził chęć zresetowania hasła
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @POST
    @Path("/reset-password/{login}")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response resetPassword(@PathParam("login") String login) {
        throw new MethodNotImplementedException();
    }

    // MOK.14 Zresetuj hasło
    /**
     * Metoda resetująca hasło użytkownika w przypadku zapomnienia
     * Metoda zmienia hasło użytkownika na nowopodane dane
     *
     * @param resetPasswordDto Obiekt DTO zawierający login, hasło oraz token do zresetowania hasła dla danego konta
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @POST
    @Path("/reset-password-token")
    @PermitAll
    @Consumes(MediaType.APPLICATION_JSON)
    default Response resetPasswordToken(@Valid ResetPasswordTokenDto resetPasswordDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.15 Przeglądaj szczegóły własnego konta
    /**
     * Metoda zwracająca dane aktualnie zalogowanego użytkownika.
     *
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @GET
    @Path("/")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    default Response getOwnAccount() {
        throw new MethodNotImplementedException();
    }

    // MOK.16 Przeglądaj szczegóły konta innego użytkownika
    /**
     * Metoda zwracająca szczegółowe informacje dotyczące wybranego konta
     *
     * @param login   Login konta, którego dane mają zostać wczytane
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @GET
    @Path("/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAccount(@PathParam("login") String login) {
        throw new MethodNotImplementedException();
    }

}

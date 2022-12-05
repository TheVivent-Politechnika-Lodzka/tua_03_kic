package pl.lodz.p.it.ssbd2022.ssbd03.mok.cdi.endpoints;

import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.AccountWithAccessLevelsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.ChangeOwnPasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.ChangePasswordDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.access_levels.AccessLevelDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.CreateAccountDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.LoginCredentialsDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.RefreshTokenDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.RegisterClientConfirmDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.RegisterClientDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag.ResetPasswordTokenDto;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public interface MOKEndpointInterface {

    // ping
    @GET
    @Path("/ping")
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    @Produces(MediaType.TEXT_PLAIN)
    default Response ping() {
        return Response.ok("pong").build();
    }

    /**
     * MOK.1 Zarejestruj
     *
     * @param registerClientDto - dane konta
     * @return Response zawierający status HTTP
     * @throws MethodNotImplementedException jeśli metoda nie została zaimplementowana
     */
    @POST
    @Path("/register")
    @RolesAllowed(Roles.ANONYMOUS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response register(@Valid RegisterClientDto registerClientDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.1 Zarejestruj - potwierdzenie rejestracji konta
     *
     * @param registerConfirmDto token
     * @return Response zawierający status HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @POST
    @Path("/register-confirm")
    @RolesAllowed(Roles.ANONYMOUS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response confirmRegistration(@Valid RegisterClientConfirmDto registerConfirmDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.2 Utwórz konto
     *
     * @param createAccountDto dane konta
     * @return Response zawierający status HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PUT
    @Path("/create")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response createAccount(@Valid CreateAccountDto createAccountDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.3 Zablokuj konto
     * Metoda blokująca konto użytkownika.
     * Wymaga nagłówka If-Match zawierającego etag blokowanego konta.
     *
     * @param login Login konta, które ma zostać zablokowane
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PATCH
    @Path("/deactivate/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response deactivateAccount(@PathParam("login") String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.4 Odblokuj konto
     * Metoda odblokowująca konto użytkownika, które zostało uprzednio zablokowane przez administratora
     * Wymaga nagłówka If-Match zawierającego eTag odblokowywanego konta
     *
     * @param login Login konta, które ma zostać odblokowane
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PATCH
    @Path("/activate/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response activateAccount(@PathParam("login") String login) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.5 Dołącz poziom dostępu do konta
     * Metoda dodająca poziom dostępu do konta użytkownika
     *
     * @param login          Login konta, które ma zostać odblokowane
     * @param accessLevelDto Obiekt DTO, zawierający informacje o dołączanym poziomie dostępu
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PUT
    @Path("/access-level/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response addAccessLevel(@PathParam("login") String login, @Valid AccessLevelDto accessLevelDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.6 Odłącz poziom dostępu od konta
     * Metoda odłączająca poziom dostępu dla konta, wywołana z poziomu endpointa.
     * Może ją tylko wykonać tylko konto z poziomem dostępu administratora.
     *
     * @param login       Login użytkownika, którego poziom dostępu ma zostać odłączony
     * @param accessLevel Poziom dostępu, który ma zostać odłączony (klient, specjalista bądź administrator)
     * @return odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     * @throws TransactionException          w momencie, gdy transakcja nie została zatwierdzona
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @DELETE
    @Path("/access-level/{login}/{accessLevel}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response removeAccessLevel(@PathParam("login") String login, @PathParam("accessLevel") String accessLevel) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.7 Zmień własne hasło
     * Metoda zmieniająca hasło aktualnego użytkownika, wywoływana z poziomu endpointa.
     * Metoda dostepna dla kont z dowolnym poziomem dostepu.
     *
     * @param changeOwnPasswordDto Obiekt Dto zawierający etag, stare (aktualne) hasło oraz nowe hasło
     * @return odpowiedź HHTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PATCH
    @Path("/password")
    @RolesAllowed(Roles.AUTHENTICATED)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response changeOwnPassword(@Valid ChangeOwnPasswordDto changeOwnPasswordDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.8 Zmień hasło innego użytkownika
     * Metoda zmieniająca hasło dowolnego użytkownika, wywoływana z poziomu endpointa.
     * Może ją tylko wykonać konto z poziomem dostępu administratora.
     *
     * @param login             Login użytkownika, któremu chcemy zmienić hasło
     * @param changePasswordDto Nowe hasło wraz z ETagiem
     * @return odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PATCH
    @Path("/password/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response changePassword(@PathParam("login") String login, @Valid ChangePasswordDto changePasswordDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.9 Edytuj dane własnego konta
     * Metoda pozwalająca na modyfikację danych
     *
     * @param accountWithAccessLevelsDto Obiekt DTO, zawierający informacje o koncie użytkownika
     * @return odpowiedź HTTP, powinna zawierać zmodyfikowane dane użytkownika
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @PUT
    @Path("/")
    @RolesAllowed(Roles.AUTHENTICATED)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response editOwnAccount(@Valid AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * Metoda pozwalająca edytować dane konta innego użytkownika, wywołana z poziomu endpointa.
     * Może ją tylko wykonać tylko konto z poziomem dostępu administratora.
     *
     * @param login                      Login użytkownika, którego dane konta zostaną edytowane
     * @param accountWithAccessLevelsDto Dane, które mają zastąpić dane konta edytowanego
     * @return odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     * @throws TransactionException          w momencie, gdy transakcja nie została zatwierdzona
     */
    @PUT
    @Path("/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response editAccount(@PathParam("login") String login, @Valid AccountWithAccessLevelsDto accountWithAccessLevelsDto) {
        throw new MethodNotImplementedException();
    }

    // MOK.11 Wyloguj
    // nie dotyczy backendu

    /**
     * MOK.12 Zaloguj
     * Metoda uwierzytelniająca użytkownika
     *
     * @param loginCredentialsDto dane logowania
     * @return Response zawierający status HTTP, accessToken oraz refreshToken
     * @throws MethodNotImplementedException jeśli metoda nie została zaimplementowana
     */
    @POST
    @Path("/login")
    @RolesAllowed(Roles.ANONYMOUS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response login(@Valid LoginCredentialsDto loginCredentialsDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * Endpoint umożliwiający odświeżanie tokenu
     *
     * @param refreshTokenDto
     * @return
     */
    @POST
    @Path("/refresh-token")
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response refreshToken(@Valid RefreshTokenDto refreshTokenDto) {
        throw new MethodNotImplementedException();
    }

    /**
     * MOK.13 przeglądaj listę wszystkich kont
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
    @Consumes(MediaType.APPLICATION_JSON)
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
    @RolesAllowed(Roles.ANONYMOUS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
    @RolesAllowed(Roles.ANONYMOUS)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
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
    @RolesAllowed(Roles.AUTHENTICATED)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response getOwnAccount() {
        throw new MethodNotImplementedException();
    }

    // MOK.16 Przeglądaj szczegóły konta innego użytkownika

    /**
     * Metoda zwracająca szczegółowe informacje dotyczące wybranego konta
     *
     * @param login Login konta, którego dane mają zostać wczytane
     * @return Odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @GET
    @Path("/{login}")
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    default Response getAccount(@PathParam("login") String login) {
        throw new MethodNotImplementedException();
    }

}

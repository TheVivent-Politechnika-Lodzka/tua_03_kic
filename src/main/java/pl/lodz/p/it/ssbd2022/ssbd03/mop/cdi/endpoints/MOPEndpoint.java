package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEndpoint;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AccountMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AppointmentMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantReviewMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RequestScoped
@DenyAll
@Path("/mop")
@TransactionAttribute(TransactionAttributeType.NEVER)
public class MOPEndpoint extends AbstractEndpoint implements MOPEndpointInterface {

    private static final long serialVersionUID = 1L;

    @Inject
    AuthContext authContext;
    @Inject
    private MOPServiceInterface mopService;
    @Inject
    private AppointmentMapper appointmentMapper;

    @Inject
    private ImplantMapper implantMapper;

    @Inject
    private AccountMapper accountMapper;

    @Inject
    private ImplantReviewMapper implantReviewMapper;

    @Inject
    private Tagger tagger;


    /**
     * MOP.1 - Dodaj nowy wszczep
     *
     * @param createImplantDto - dane nowego wszczepu
     * @return odpowiedź zawierająca status http
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response createImplant(CreateImplantDto createImplantDto) {
        Implant implant = implantMapper.createImplantFromDto(createImplantDto);

        Implant createdImplant = repeat(mopService, () -> mopService.createImplant(implant));

        ImplantDto implantDto = implantMapper.createImplantDtoFromImplant(createdImplant);

        return Response.ok(implantDto).tag(tagger.tag(implantDto)).build();
    }

    /**
     * MOP 2 - Archiwizuj wszczep
     *
     * @param id - uuid wszczepu poddawanego archiwizacji
     * @return odpowiedź zawieracjąca status http oraz nowy tag
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response archiveImplant(UUID id) {
        tagger.verifyTag();
        Implant archivedImplant = repeat(mopService, () -> mopService.archiveImplant(id));

        ImplantDto imp = implantMapper.createImplantDtoFromImplant(archivedImplant);

        return Response.ok(imp).tag(tagger.tag(imp)).build();
    }

    //MOP.3 -Edytuj wszczep
    @Override
    public Response editImplant(UUID id, ImplantDto implantDto) {
        tagger.verifyTag(implantDto);

        Implant implant = repeat(mopService, () -> mopService.editImplant(id, implantMapper.createImplantFromImplantDto(implantDto)));

        ImplantDto updatedImplant = implantMapper.createImplantDtoFromImplant(implant);

        return Response.ok(updatedImplant).tag(tagger.tag(updatedImplant)).build();
    }

    //MOP.4 - Przegladaj szczegoły wszczepu
    @Override
    public Response getImplant(UUID id) {
        Implant implant = repeat(mopService, () -> mopService.findImplantByUuid(id));

        ImplantDto dto = implantMapper.createImplantDtoFromImplant(implant);
        return Response.ok(dto).tag(tagger.tag(dto)).build();
    }

    /**
     * MOK.5 - Przeglądaj listę wszczepów
     *
     * @param page     numer strony
     * @param size     ilość pozycji na stronie
     * @param phrase   szukana fraza
     * @param archived określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response listImplants(int page, int size, String phrase, boolean archived) {
        PaginationData paginationData = repeat(mopService, () -> mopService.findImplants(page, size, phrase, archived));

        List<Implant> implants = paginationData.getData();
        List<ImplantDto> implantsDto = implantMapper.getListFromImplantListElementDtoFromImplant(implants);
        paginationData.setData(implantsDto);
        return Response.ok().entity(paginationData).build();
    }

    /**
     * MOP.6 - Przeglądaj listę specialistów
     * dostęp posiadają wszyscy użytkownicy serwisu włącznie z nieuwierzytelnionymi
     *
     * @param page   - numer strony (int)
     * @param size   - ilość specialistó wyświetlanych na jednej stronie (int)
     * @param phrase - szukana fraza specialisty (String)
     * @return zwraca odpowiedz zawierającą listę specialistów
     * @throws TransactionException błąd transakcji
     */
    @Override
    public Response listSpecialists(int page, int size, String phrase) {
        PaginationData paginationData = repeat(mopService, () -> mopService.findSpecialists(page, size, phrase));

        List<Account> accounts = paginationData.getData();
        List<SpecialistForMopDto> accountsDTO = accountMapper.accountSpecialistListElementDtoList(accounts);
        paginationData.setData(accountsDTO);
        return Response.ok().entity(paginationData).build();

    }

    /**
     * MOP.7 - Przeglądaj listę wizyt
     *
     * @param page   numer aktualnie przeglądanej strony
     * @param size   ilość rekordów na danej stronie
     * @param phrase wyszukiwana fraza
     * @return lista wizyt
     * @throws TransactionException w przypadku braku zatwierdzenia transakcji
     */
    @Override
    public Response listAppointments(int page, int size, String phrase) {
        PaginationData paginationData = repeat(mopService, () -> mopService.findVisits(page, size, phrase));

        List<Appointment> appointments = paginationData.getData();
        List<AppointmentListElementDto> appointmentDtos = appointmentMapper.appointmentListElementDtoList(appointments);
        paginationData.setData(appointmentDtos);
        return Response.ok().entity(paginationData).build();
    }

    /**
     * MOP.8 - Przeglądaj swoje wizyty
     *
     * @param page numer aktualnie przeglądanej strony
     * @param size ilość rekordów na danej stronie
     * @return lista wizyt
     * @throws TransactionException w przypadku braku zatwierdzenia transakcji
     */
    @Override
    public Response listMyAppointments(int page, int size) {
        String login = authContext.getCurrentUserLogin();
        PaginationData paginationData = repeat(mopService, () -> mopService.findVisitsByLogin(page, size, login));

        List<Appointment> appointments = paginationData.getData();
        List<AppointmentListElementDto> appointmentDtos = appointmentMapper.appointmentListElementDtoList(appointments);
        paginationData.setData(appointmentDtos);
        return Response.ok().entity(paginationData).build();
    }

    /**
     * MOP.9 - Zarezerwuj wizytę
     *
     * @param createAppointmentDto - dane nowej wizyty
     * @return status HTTP i utworzoną wizytę
     * @throws TransactionException, w przypadku odrzucenia transakcji z nieznanego powodu
     */
    @Override
    public Response createAppointment(CreateAppointmentDto createAppointmentDto) {

        String clientLogin = authContext.getCurrentUserLogin();
        UUID specialistId = createAppointmentDto.getSpecialistId();
        UUID implantId = createAppointmentDto.getImplantId();
        Instant startDate = createAppointmentDto.getStartDate();

        Appointment createdAppointment = repeat(mopService, () -> mopService.createAppointment(
                clientLogin,
                specialistId,
                implantId,
                startDate
        ));

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(createdAppointment);

        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }

    /**
     * MOP.9 - Zarezerwuj wizytę, dostępność specjalisty
     *
     * @param specialistId - id specjalisty
     * @param month        - miesiąc wizyty (Instant)
     * @param duration     - długość wizyty
     * @return lista dostępności
     * @throws TransactionException w przypadku braku zatwierdzenia transakcji
     */
    @Override
    public Response getSpecialistAvailability(UUID specialistId, String month, int duration) {

        List<Instant> availability;
        Duration durationObj = Duration.ofSeconds(duration);
        Instant monthObj = Instant.parse(month);

        availability = repeat(mopService, () -> mopService.getSpecialistAvailabilityInMonth(specialistId, monthObj, durationObj));

        return Response.ok(availability).build();
    }

    /**
     * MOP.10 - Edytuj swoją wizytę
     *
     * @param id                    id konkretnej wizyty
     * @param appointmentOwnEditDto obiekt dto edycji naniesionych do wizyty
     * @return odpowiedz HTTP
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response editOwnAppointment(UUID id, AppointmentOwnEditDto appointmentOwnEditDto) {
        tagger.verifyTag(appointmentOwnEditDto);
        String login = authContext.getCurrentUserLogin();
        Appointment update = appointmentMapper.createAppointmentFromAppointmentOwnEditDto(appointmentOwnEditDto);
        Appointment editedAppointment = repeat(mopService, () -> mopService.editOwnAppointment(id, update, login));

        AppointmentDto app = appointmentMapper.createAppointmentDtoFromAppointment(editedAppointment);
        return Response.ok(app).tag(tagger.tag(app)).build();
    }

    /**
     * MOP.11 - Edytuj dowolną wizytę
     *
     * @param id                 id konkretnej wizyty
     * @param appointmentEditDto obiekt dto edytowanej wizyty
     * @return odpowiedz HTTP
     */
    @Override
    public Response editAppointment(UUID id, AppointmentEditDto appointmentEditDto) {
        tagger.verifyTag(appointmentEditDto);

        Appointment update = appointmentMapper.createAppointmentFromEditDto(appointmentEditDto);
        Appointment editedAppointment = repeat(mopService, () -> mopService.editAppointmentByAdministrator(id, update));

        AppointmentEditDto app = appointmentMapper.createEditDtoFromAppointment(editedAppointment);

        return Response.ok(app).tag(tagger.tag(app)).build();
    }

    /**
     * MOP.12 - Odwołaj swoją wizytę
     * Endpoint pozwalający odwołać wizytę (REJECTED)
     *
     * @param id - id wizyty
     * @return status HTTP oraz zmodyfikowana wizyta
     * @throws TransactionException, gdy transakcja się nie powiedzie
     */
    @Override
    public Response cancelOwnAppointment(UUID id) {
        tagger.verifyTag();
        Appointment cancelledAppointment = repeat(mopService, () -> mopService.cancelOwnAppointment(id));

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(cancelledAppointment);

        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }


    /**
     * MOP.13 Odwołaj dowolną wizytę
     * Metodę może wykonać tylko konto z poziomem dostępu administratora.
     *
     * @param id Identyfikator wizyty, która ma zostać odwołana
     * @return odpowiedź HTTP
     */
    @Override
    public Response cancelAnyAppointment(UUID id) {
        tagger.verifyTag();
        Appointment cancelledAppointment = repeat(mopService, () -> mopService.cancelAnyAppointment(id));

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(cancelledAppointment);

        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }

    /**
     * MOP.14 - Oznacz wizytę jako zakończoną
     *
     * @param id identyfikator wizyty, która ma zostać oznaczona jako zakończona
     * @return zakończona wizyta
     */
    @Override
    public Response finishAppointment(UUID id) {
        tagger.verifyTag();

        String login = authContext.getCurrentUserLogin();
        Appointment finishedAppointment = repeat(mopService, () -> mopService.finishAppointment(id, login), List.of(InAppOptimisticLockException.class));

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(finishedAppointment);
        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }

    /**
     * MOP.15 - Dodaj recenzję wszczepu
     *
     * @param createImplantReviewDto - Nowo napisana recenzja
     * @return nowo utworzona recenzja
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response addImplantsReview(CreateImplantReviewDto createImplantReviewDto) {
        ImplantReview implantReview = implantReviewMapper.createImplantReviewFromDto(createImplantReviewDto);
        ImplantReview createdReview = repeat(mopService, () -> mopService.createReview(implantReview));

        ImplantReviewDto createdReviewDto = implantReviewMapper.implantReviewDtofromImplantReview(createdReview);
        return Response.ok().entity(createdReviewDto).build();
    }

    /**
     * MOK.16 - Usuń recenzję wszczepu
     *
     * @param id Id recenzji wszczepu
     * @return Odpowiedź HTTP
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response deleteImplantsReview(UUID id) {
        String login = authContext.getCurrentUserLogin();
        repeat(mopService, () -> mopService.deleteReview(id, login));

        return Response.ok().build();
    }

    @Override
    public Response getAppointmentDetails(UUID uuid) {
        String login = authContext.getCurrentUserLogin();
        Appointment appointment = repeat(mopService, () -> mopService.findVisit(uuid, login));

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(appointment);
        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }

    /**
     * MOP.18 - Wyświetl recenzje dla danego wszczepu
     *
     * @param size Ilość recenzji do wyświetlenia na jednej stronie
     * @param page Numer strony
     * @param id   Identyfikator wszczepu
     * @return Lista recenzji wszczepu
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response getAllImplantReviews(int page, int size, UUID id) {
        PaginationData paginationData = repeat(mopService, () -> mopService.getAllImplantReviews(page, size, id));

        List<ImplantReview> implantReviews = paginationData.getData();
        List<ImplantReviewDto> implantReviewDtos = implantReviewMapper.implantReviewDtoListfromImplantReviewList(implantReviews);
        paginationData.setData(implantReviewDtos);
        return Response.ok().entity(paginationData).build();
    }
}

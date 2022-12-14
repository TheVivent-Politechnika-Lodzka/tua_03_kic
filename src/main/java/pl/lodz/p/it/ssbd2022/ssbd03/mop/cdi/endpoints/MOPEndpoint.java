package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractEndpoint;
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
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentEditDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentListElementDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentOwnEditDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateAppointmentDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.SpecialistForMopDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import javax.annotation.security.DenyAll;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
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
     * @return odpowied?? zawieraj??ca status http
     * @throws TransactionException je??li transakcja nie zosta??a zatwierdzona
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
     * @return odpowied?? zawieracj??ca status http oraz nowy tag
     * @throws TransactionException je??li transakcja nie zosta??a zatwierdzona
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

    //MOP.4 - Przegladaj szczego??y wszczepu
    @Override
    public Response getImplant(UUID id) {
        Implant implant = repeat(mopService, () -> mopService.findImplantByUuid(id));

        ImplantDto dto = implantMapper.createImplantDtoFromImplant(implant);
        return Response.ok(dto).tag(tagger.tag(dto)).build();
    }

    /**
     * MOK.5 - Przegl??daj list?? wszczep??w
     *
     * @param page     numer strony
     * @param size     ilo???? pozycji na stronie
     * @param phrase   szukana fraza
     * @param archived okre??la czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczep??w
     * @throws TransactionException je??li transakcja nie zosta??a zatwierdzona
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
     * MOP.6 - Przegl??daj list?? specialist??w
     * dost??p posiadaj?? wszyscy u??ytkownicy serwisu w????cznie z nieuwierzytelnionymi
     *
     * @param page   - numer strony (int)
     * @param size   - ilo???? specialist?? wy??wietlanych na jednej stronie (int)
     * @param phrase - szukana fraza specialisty (String)
     * @return zwraca odpowiedz zawieraj??c?? list?? specialist??w
     * @throws TransactionException b????d transakcji
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
     * MOP.7 - Przegl??daj list?? wizyt
     *
     * @param page   numer aktualnie przegl??danej strony
     * @param size   ilo???? rekord??w na danej stronie
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
     * MOP.8 - Przegl??daj swoje wizyty
     *
     * @param page numer aktualnie przegl??danej strony
     * @param size ilo???? rekord??w na danej stronie
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
     * MOP.9 - Zarezerwuj wizyt??
     *
     * @param createAppointmentDto - dane nowej wizyty
     * @return status HTTP i utworzon?? wizyt??
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
     * MOP.9 - Zarezerwuj wizyt??, dost??pno???? specjalisty
     *
     * @param specialistId - id specjalisty
     * @param month        - miesi??c wizyty (Instant)
     * @param duration     - d??ugo???? wizyty
     * @return lista dost??pno??ci
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
     * MOP.10 - Edytuj swoj?? wizyt??
     *
     * @param id                    id konkretnej wizyty
     * @param appointmentOwnEditDto obiekt dto edycji naniesionych do wizyty
     * @return odpowiedz HTTP
     * @throws TransactionException je??li transakcja nie zosta??a zatwierdzona
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
     * MOP.11 - Edytuj dowoln?? wizyt??
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
     * MOP.12 - Odwo??aj swoj?? wizyt??
     * Endpoint pozwalaj??cy odwo??a?? wizyt?? (REJECTED)
     *
     * @param id - id wizyty
     * @return status HTTP oraz zmodyfikowana wizyta
     * @throws TransactionException, gdy transakcja si?? nie powiedzie
     */
    @Override
    public Response cancelOwnAppointment(UUID id) {
        tagger.verifyTag();
        Appointment cancelledAppointment = repeat(mopService, () -> mopService.cancelOwnAppointment(id));

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(cancelledAppointment);

        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }


    /**
     * MOP.13 Odwo??aj dowoln?? wizyt??
     * Metod?? mo??e wykona?? tylko konto z poziomem dost??pu administratora.
     *
     * @param id Identyfikator wizyty, kt??ra ma zosta?? odwo??ana
     * @return odpowied?? HTTP
     */
    @Override
    public Response cancelAnyAppointment(UUID id) {
        tagger.verifyTag();
        Appointment cancelledAppointment = repeat(mopService, () -> mopService.cancelAnyAppointment(id));

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(cancelledAppointment);

        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }

    /**
     * MOP.14 - Oznacz wizyt?? jako zako??czon??
     *
     * @param id identyfikator wizyty, kt??ra ma zosta?? oznaczona jako zako??czona
     * @return zako??czona wizyta
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
     * MOP.15 - Dodaj recenzj?? wszczepu
     *
     * @param createImplantReviewDto - Nowo napisana recenzja
     * @return nowo utworzona recenzja
     * @throws TransactionException je??li transakcja nie zosta??a zatwierdzona
     */
    @Override
    public Response addImplantsReview(CreateImplantReviewDto createImplantReviewDto) {
        ImplantReview implantReview = implantReviewMapper.createImplantReviewFromDto(createImplantReviewDto);
        ImplantReview createdReview = repeat(mopService, () -> mopService.createReview(implantReview));

        ImplantReviewDto createdReviewDto = implantReviewMapper.implantReviewDtofromImplantReview(createdReview);
        return Response.ok().entity(createdReviewDto).build();
    }

    /**
     * MOK.16 - Usu?? recenzj?? wszczepu
     *
     * @param id Id recenzji wszczepu
     * @return Odpowied?? HTTP
     * @throws TransactionException je??li transakcja nie zosta??a zatwierdzona
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
     * MOP.18 - Wy??wietl recenzje dla danego wszczepu
     *
     * @param size Ilo???? recenzji do wy??wietlenia na jednej stronie
     * @param page Numer strony
     * @param id   Identyfikator wszczepu
     * @return Lista recenzji wszczepu
     * @throws TransactionException je??li transakcja nie zosta??a zatwierdzona
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

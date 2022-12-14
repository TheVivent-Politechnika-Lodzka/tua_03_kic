package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.ACCEPTED;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.FINISHED;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.PENDING;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.REJECTED;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentCannotBeCancelledAnymoreException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentCannotBeChangedAnymoreException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentDoesNotBelongToYouException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentFinishAttemptBeforeEndDateException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentFinishAttemptByInvalidSpecialistException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentNotFinishedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.CantInstallArchivedImplant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.ImproperAccessLevelException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.SpecialistHasNoTimeException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.StartDateIsInPast;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.UserNotPartOfAppointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantArchivedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review.ClientRemovesOtherReviewsException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AccountMOPFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AppointmentMOPFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantMOPFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantReviewMOPFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import javax.annotation.security.DenyAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MOPService extends AbstractService implements MOPServiceInterface, SessionSynchronization {

    private static final long serialVersionUID = 1L;

    protected static final Logger LOGGER = Logger.getGlobal();

    @Inject
    AppointmentMOPFacade appointmentFacade;

    @Inject
    private ImplantMOPFacade implantFacade;

    @Inject
    private ImplantReviewMOPFacade implantReviewFacade;

    @Inject
    private AccountMOPFacade accountFacade;

    @Inject
    private AuthContext authContext;


    /**
     * Metoda pozwalaj??ca na odwo??anie dowolnej wizyty, wywo??ywana z poziomu serwisu.
     * Mo??e j?? wykona?? tylko konto z poziomem dost??pu administratora
     *
     * @param id identyfikator wizyty, kt??ra ma zosta?? odwo??ana
     * @return Wizyta, kt??ra zosta??a odwo??ana
     * @throws AppointmentStatusException - gdy wizyta jest ju?? zako??czona (wykoana/odwo??ana)
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Appointment cancelAnyAppointment(UUID id) {

        Appointment appointment = appointmentFacade.findById(id);
        if (appointment.getStatus().equals(REJECTED))
            throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        if (appointment.getStatus().equals(FINISHED))
            throw AppointmentStatusException.appointmentStatusAlreadyFinished();
        if (Instant.now().isAfter(appointment.getEndDate())) {
            throw new AppointmentCannotBeCancelledAnymoreException();
        }

        appointment.setStatus(REJECTED);
        appointmentFacade.edit(appointment);

        return appointment;
    }

    /**
     * Metoda pozwalaj??ca na odwo??anie w??asnej wizyty, wywo??ywana z poziomu serwisu.
     * Mo??e j?? wykona?? tylko konto z poziomem dost??pu klienta/specjalisty
     *
     * @param id identyfikator wizyty, kt??ra ma zosta?? odwo??ana
     * @return Wizyta, kt??ra zosta??a odwo??ana
     * @throws AppointmentStatusException,                   gdy wizyta jest ju?? zako??czona (wykonana/odwo??ana)
     * @throws AppointmentDoesNotBelongToYouException,       gdy wizyta nie nale??y do Ciebie
     * @throws AppointmentCannotBeCancelledAnymoreException, gdy wizyta nie mo??e zosta?? odwo??ana
     */
    @Override
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    public Appointment cancelOwnAppointment(UUID id) {
        Account thisAccount = authContext.getCurrentUser();
        Appointment appointment = appointmentFacade.findById(id);

        // sprawdzenie czy wizyta nie jest ju?? zako??czona
        if (appointment.getStatus().equals(REJECTED))
            throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        if (appointment.getStatus().equals(FINISHED))
            throw AppointmentStatusException.appointmentStatusAlreadyFinished();

        // sprawdzenie czy wizyta nale??y do tego konta
        if (!(
                appointment.getClient().getId().equals(thisAccount.getId())
                        || appointment.getSpecialist().getId().equals(thisAccount.getId())
        ))
            throw new AppointmentDoesNotBelongToYouException();

        // sprawdzenie czy wizyt?? mo??na anulowa?? (mo??na maksymalnie dzie?? wcze??niej)
        LocalDate today = LocalDate.now();
        LocalDate appointmentDate = LocalDate.ofInstant(appointment.getStartDate(), ZoneId.systemDefault());
        if (today.getDayOfYear() >= appointmentDate.getDayOfYear())
            throw new AppointmentCannotBeCancelledAnymoreException();

        appointment.setStatus(REJECTED);
        appointmentFacade.edit(appointment);

        return appointment;
    }

    /**
     * Metoda tworz??ca nowy wszczep
     *
     * @param implant - nowy wszczep
     * @return Implant
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Implant createImplant(Implant implant) {
        implantFacade.create(implant);
        return implantFacade.findByUUID(implant.getId());
    }

    /**
     * Serwis odpowiadaj??cy za archiwizacj?? wszczepu - MOP. 2
     *
     * @param id - uuid archiwizowanego wszczepu
     * @return tmp - zarchiwizowany Implant
     * @throws ImplantStatusException je??eli archiwizowany wszczep jest ju?? zarchiwizowany
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Override
    public Implant archiveImplant(UUID id) {
        Implant tmp = implantFacade.findByUUID(id);
        if (tmp.isArchived()) {
            throw ImplantStatusException.implantArleadyArchive();
        }
        tmp.setArchived(true);
        implantFacade.edit(tmp);
        return tmp;
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Implant editImplant(UUID uuid, Implant implant) {
        Implant implantFromDB = implantFacade.findByUUID(uuid);

        if (implantFromDB.isArchived()) {
            throw ImplantArchivedException.editArchivedImplant();
        }

        implantFromDB.setName(implant.getName());
        implantFromDB.setDescription(implant.getDescription());
        implantFromDB.setImage(implant.getImage());
        implantFromDB.setDuration(implant.getDuration());
        implantFromDB.setManufacturer(implant.getManufacturer());
        implantFromDB.setPrice(implant.getPrice());

        implantFacade.edit(implantFromDB);
        return implantFromDB;
    }

    /**
     * Metoda zwracaj??ca liste wszczep??w
     *
     * @param page     numer strony
     * @param pageSize ilo???? pozycji na stronie na stronie
     * @param phrase   szukana fraza
     * @param archived okre??la czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczep??w
     * @throws InvalidParametersException je??li podano nieprawid??owe parametry
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public PaginationData findImplants(int page, int pageSize, String phrase, boolean archived) {
        if (page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return implantFacade.findInRangeWithPhrase(page, pageSize, phrase, archived);
    }

    /**
     * Metoda zapewniaj??ca mo??liwo???? oznaczenia wizyty jako zako??czonej
     *
     * @param id    identyfikator wizyty
     * @param login login specjalisty oznaczaj??cego wizyt?? jako zako??czon??
     * @return wizyta oznaczona jako zako??czona
     * @throws AppointmentFinishAttemptByInvalidSpecialistException gdy specjalista pr??buje zako??czy?? wizyt?? inn?? ni?? w??asna
     * @throws AppointmentStatusException                           gdy wizyta jest ju?? odwo??ana b??d?? oznaczona jako zako??czona
     * @throws AppointmentFinishAttemptBeforeEndDateException       gdy specjalista pr??buje oznaczyc wizyt?? jako zako??czon?? przed dat?? zako??czenia
     */
    @RolesAllowed(Roles.SPECIALIST)
    @Override
    public Appointment finishAppointment(UUID id, String login) {
        Appointment appointment = appointmentFacade.findById(id);
        if (!appointment.getSpecialist().getLogin().equals(login))
            throw new AppointmentFinishAttemptByInvalidSpecialistException();
        if (appointment.getStatus().equals(REJECTED))
            throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        if (appointment.getStatus().equals(FINISHED))
            throw AppointmentStatusException.appointmentStatusAlreadyFinished();
        if (appointment.getEndDate().isAfter(Instant.now())) throw new AppointmentFinishAttemptBeforeEndDateException();

        appointment.setStatus(FINISHED);

        Implant implant = appointment.getImplant();
        implant.setPopularity(implant.getPopularity() + 1);

        appointmentFacade.edit(appointment);

        return appointment;
    }

    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public Implant findImplantByUuid(UUID uuid) {
        return implantFacade.findByUUID(uuid);
    }

    /**
     * Metoda tworz??ca recenzj?? wszczepu oraz zwracaj??ca nowo utworzon?? recenzj??.
     * Recenzja nie mo??e by?? utworzona, gdy wszczep nie zosta?? jeszcze wmontowany.
     *
     * @param review - Recenzja wszczepu
     * @return Nowo utworzona recenzja wszczepu
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public ImplantReview createReview(ImplantReview review) {
        // TODO: stworzy?? fasad?? z named query kt??re b??dzie szuka?? po loginie i id wszczepu
        //  przeszukiwanie w ten spos??b jest BARDZO zasobo??erne przy wi??kszej ilo??ci recenzji
        Appointment clientAppointment = appointmentFacade.findByClientLogin(review.getClient().getLogin())
                .stream()
                .filter(appointment -> appointment.getImplant().getId().equals(review.getImplant().getId()))
                .findFirst()
                .orElseThrow(AppointmentNotFoundException::new);

        if (!clientAppointment.getStatus().equals(Status.FINISHED)) {
            throw new AppointmentNotFinishedException();
        }

        implantReviewFacade.create(review);
        return implantReviewFacade.findByUUID(review.getId());
    }

    /**
     * Metoda zwracaj??ca liste wizyt
     *
     * @param page     numer aktualnie przegl??danej strony
     * @param pageSize ilo???? rekord??w na danej stronie
     * @param phrase   wyszukiwana fraza
     * @return Lista wizyt zgodnych z parametrami wyszukiwania
     * @throws InvalidParametersException w przypadku podania nieprawid??owych parametr??w
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public PaginationData findVisits(int page, int pageSize, String phrase) {
        if (page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return appointmentFacade.findInRangeWithPhrase(page, pageSize, phrase);
    }

    @Override
    @RolesAllowed(Roles.AUTHENTICATED)
    public Appointment findVisit(UUID uuid, String clientLogin) {
        Account account = accountFacade.findByLogin(clientLogin);
        Appointment appointment = appointmentFacade.findById(uuid);
        if (!account.isInRole(Roles.ADMINISTRATOR)) {
            if (!(appointment.getClient().getLogin().equals(clientLogin)
                    || appointment.getSpecialist().getLogin().equals(clientLogin))) {
                throw new UserNotPartOfAppointment();
            }
            return appointment;
        }
        return appointment;
    }

    /**
     * Metoda zwracaj??ca edytowan?? wizyt??
     *
     * @param id     id wizyty
     * @param update warto??ci kt??re maj?? zosta?? zaktualizowane
     * @param login  nazwa uzytkownika kt??ry bierze udzia?? w wizycie
     * @return Edytowana wizyta
     * @throws UserNotPartOfAppointment   w przypadku gdy u??ytkownik edytuje nie swoja wizyt??
     * @throws AppointmentStatusException w przypadku gdy u??ytkownik chce edytowa?? zako??czon?? lub odrzucon?? wizyt??
     */
    @Override
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    public Appointment editOwnAppointment(UUID id, Appointment update, String login) {
        Appointment appointmentFromDb = appointmentFacade.findById(id);
        boolean didStartDateChange = false;
        if (!(appointmentFromDb.getClient().getLogin().equals(login) || appointmentFromDb.getSpecialist().getLogin().equals(login))) {
            throw new UserNotPartOfAppointment();
        }
        if (appointmentFromDb.getStatus().equals(FINISHED)) {
            throw AppointmentStatusException.appointmentStatusAlreadyFinished();
        }
        if (appointmentFromDb.getStatus().equals(REJECTED)) {
            throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        }
        // sprawdzenie czy mozna edytowa?? (musi by?? wi??cej ni?? jeden dzie??)
        LocalDate today = LocalDate.now();
        LocalDate appointmentDate = LocalDate.ofInstant(appointmentFromDb.getStartDate(), ZoneId.systemDefault());
        if (today.getDayOfYear() >= appointmentDate.getDayOfYear())
            throw new AppointmentCannotBeChangedAnymoreException();

        if (!update.getStartDate().equals(appointmentFromDb.getStartDate())) {
            Instant endDate = update.getStartDate().plus(appointmentFromDb.getImplantDuration());
            checkDateAvailabilityForAppointment(appointmentFromDb.getSpecialist().getId(), update.getStartDate(), endDate);
            appointmentFromDb.setStartDate(update.getStartDate());
            appointmentFromDb.setEndDate(endDate);
            didStartDateChange = true;
        }
        if (appointmentFromDb.getClient().getLogin().equals(login) && didStartDateChange) {
            appointmentFromDb.setStatus(PENDING);
        } else {
            appointmentFromDb.setDescription(update.getDescription());
            if (update.getStatus().equals(ACCEPTED)) {
                appointmentFromDb.setStatus(ACCEPTED);
            }
        }
        appointmentFacade.edit(appointmentFromDb);
        return appointmentFromDb;
    }

    @Override
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    public PaginationData findVisitsByLogin(int page, int pageSize, String login) {
        if (page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return appointmentFacade.findByClientLoginInRange(page, pageSize, login);
    }

    /**
     * Metoda zwracaj??ca liste specialist??w - MOP.6
     * dost??p dla wszytskich
     *
     * @param page     - numer strony (int)
     * @param pageSize - ilo???? rekord??w na stronie (int)
     * @param phrase   - szukana fraza (String)
     * @return - lista specialist??w (PaginationData)
     * @throws InvalidParametersException przy podaniu b????dnych parametr??w
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public PaginationData findSpecialists(int page, int pageSize, String phrase) {
        if (page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return accountFacade.findInRangeWithPhrase(page, pageSize, phrase);
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Appointment editAppointmentByAdministrator(UUID uuid, Appointment appointment) {
        Appointment appointmentFromDb = appointmentFacade.findById(uuid);
        if (Instant.now().isAfter(appointmentFromDb.getEndDate())) {
            throw new AppointmentCannotBeChangedAnymoreException();
        }
        appointmentFromDb.setDescription(appointment.getDescription());
        if (appointmentFromDb.getStatus() == Status.ACCEPTED) {
            appointmentFromDb.setStatus(appointment.getStatus());
        }
        appointmentFacade.edit(appointmentFromDb);
        return appointmentFromDb;
    }

    /**
     * Metoda usuwaj??ca recenzj?? wszczepu
     *
     * @param id Identyfikator recenzji wszczepu, kt??ra ma zosta?? usuni??ta
     */
    @Override
    @RolesAllowed({Roles.ADMINISTRATOR, Roles.CLIENT})
    public boolean deleteReview(UUID id, String login) {

        ImplantReview review = implantReviewFacade.findByUUID(id);
        Account account = accountFacade.findByLogin(login);
        boolean isAdmin = account.getAccessLevelCollection()
                .stream()
                .anyMatch(accessLevel -> accessLevel.getLevel().equals(Roles.ADMINISTRATOR));

        if (!review.getClient().getLogin().equals(login) && !isAdmin) {
            throw new ClientRemovesOtherReviewsException();
        }
        implantReviewFacade.remove(review);
        return true;
    }


    /**
     * Metoda tworz??ca now?? wizyt??
     *
     * @param clientLogin  - login klienta
     * @param specialistId - identyfikator specjalisty
     * @param implantId    - identyfikator wszczepu
     * @param startDate    - data rozpocz??cia wizyty
     * @return nowa wizyta
     * @throws ImproperAccessLevelException - w przypadku pr??by przypisanie u??ytkownika do z??ej roli w wizycie
     * @throws CantInstallArchivedImplant   - w przypadku kiedy wszczep jest zarchiwizowany
     * @throws StartDateIsInPast            - w przypadku gdy podana data jest dat?? z przesz??o??ci
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public Appointment createAppointment(String clientLogin, UUID specialistId, UUID implantId, Instant startDate) {
        Appointment appointment = new Appointment();

        // weryfikacja czy klient posiada rol?? CLIENT (technicznie nie ma sensu, ale lepiej, ??eby by??o)
        Account client = accountFacade.findByLogin(clientLogin);
        if (!client.isInRole(Roles.CLIENT)) {
            throw ImproperAccessLevelException.accountNotClient();
        }
        appointment.setClient(accountFacade.findByLogin(clientLogin));

        // weryfikacja czy specjalista posiada rol?? SPECIALIST
        Account specialist = accountFacade.findByUUID(specialistId);
        if (!specialist.isInRole(Roles.SPECIALIST)) {
            throw ImproperAccessLevelException.accountNotSpecialist();
        }
        appointment.setSpecialist(accountFacade.findByUUID(specialistId));

        // weryfikacja czy wszczep istnieje i nie jest zarchiwizowany
        Implant implant = implantFacade.findByUUID(implantId);
        if (implant.isArchived()) {
            throw new CantInstallArchivedImplant();
        }
        // ta metoda (v) od razu archiwizuje wszystkie dane a propos wszczepu do wizyty
        appointment.setImplant(implantFacade.findByUUID(implantId));

        // wryfikacja czy data rozpocz??cia wizyty jest p????niejsza ni?? data aktualna
        if (startDate.isBefore(Instant.now())) {
            throw new StartDateIsInPast();
        }
        appointment.setStartDate(startDate);
        Instant endDate = startDate.plus(appointment.getImplant().getDuration());
        appointment.setEndDate(endDate);

        // proste settery
        appointment.setDescription("");
        appointment.setPrice(appointment.getImplant().getPrice());

        checkDateAvailabilityForAppointment(specialistId, startDate, endDate);

        appointmentFacade.create(appointment);
        return appointment;
    }

    /**
     * MOP.9 - Zarezerwuj wizyt??, dost??pno???? specjalisty
     *
     * @param specialistId - id specjalisty
     * @param month        - miesi??c
     * @return lista dost??pno??ci
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public List<Instant> getSpecialistAvailabilityInMonth(UUID specialistId, Instant month, Duration duration) {
        // wyci??gni??cie ilo??ci dni w danym miesi??cu (trzeba tak, bo trzeba pami??ta??, ??e ka??dy miesi??c ma inn?? ilo???? dni)
        // oraz istniej?? lata przest??pne
        int daysInMonth = YearMonth.from(month.atZone(ZoneId.systemDefault())).lengthOfMonth();
        // wyci??gni??cie pierwszego dnia miesi??ca
        LocalDate startLocalDate = YearMonth.from(month.atZone(ZoneId.systemDefault())).atDay(1);
        Instant startDate = startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Instant> availableDates = new ArrayList<>();

        // Za??o??enie jest takie, ??e wszystkie dni miesi??ca, s?? pracuj??ce.
        // Przeszukiwane s?? godziny od 8:00 do 15:00 ka??dego dnia
        // regu??a biznesowa: je??eli zabieg zaczyna si?? przed 16:00, to mo??e trwa?? nawet d??u??ej ni?? do 16:00
        int i = 0;
        for (; i < daysInMonth; i++) {
            Instant day = startDate.plus(i, ChronoUnit.DAYS);

            for (int j = 0; j < 8; j++) {
                Instant start = day.plus(j + 8, ChronoUnit.HOURS);
                Instant end = start.plus(duration);
                try {
                    checkDateAvailabilityForAppointment(specialistId, start, end);
                    availableDates.add(start);
                } catch (SpecialistHasNoTimeException e) {
                    // do nothing
                }
            }
        }
        return availableDates;
    }

    /**
     * Metoda zwracaj??ca list?? wszystkich recenzji dla danego wszczepu
     *
     * @param page     Aktualny numer strony
     * @param pageSize Ilo???? recenzji na pojedynczej stronie
     * @param id       Identyfikator wszczepu
     * @return Lista recenzji dla wszczepu
     * @throws InvalidParametersException przy podaniu b????dnych parametr??w
     */
    @Override
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public PaginationData getAllImplantReviews(int page, int pageSize, UUID id) {
        if (page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return implantReviewFacade.findInRangeWithPhrase(page, pageSize, id);
    }

    /**
     * Metoda weryfikuj??ca, czy specjalista ma czas na wizyt?? w danym terminie
     *
     * @param specialistId - identyfikator specjalisty
     * @param startDate    - data rozpocz??cia wizyty
     * @param endDate      - data zako??czenia wizyty
     * @throws SpecialistHasNoTimeException w przypadku, gdy specjalista nie ma czasu na wizyt?? (appBase)
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    private void checkDateAvailabilityForAppointment(UUID specialistId, Instant startDate, Instant endDate) {
        PaginationData appointments = appointmentFacade.findSpecialistAppointmentsInGivenPeriod(specialistId, startDate, endDate, 1, 1);
        if (appointments.getData().size() > 0) {
            throw new SpecialistHasNoTimeException();
        }
    }
}

package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentFinishAttemptBeforeEndDateException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentFinishAttemptByInvalidSpecialistException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentNotFinishedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantArchivedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review.ClientRemovesOtherReviewsException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantReviewFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AppointmentFacade;

import javax.management.relation.Role;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.FINISHED;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.REJECTED;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import java.util.logging.Logger;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.*;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class MOPService extends AbstractService implements MOPServiceInterface, SessionSynchronization {

    private static final long serialVersionUID = 1L;

    protected static final Logger LOGGER = Logger.getGlobal();

    @Inject
    AppointmentFacade appointmentFacade;

    @Inject
    private ImplantFacade implantFacade;

    @Inject
    private ImplantReviewFacade implantReviewFacade;

    @Inject
    private AccountFacade accountFacade;

    @Inject
    private AuthContext authContext;


    /**
     * Metoda pozwalająca na odwołanie dowolnej wizyty, wywoływana z poziomu serwisu.
     * Może ją wykonać tylko konto z poziomem dostępu administratora
     *
     * @param id identyfikator wizyty, która ma zostać odwołana
     * @return Wizyta, która została odwołana
     * @throws AppointmentStatusException - gdy wizyta jest już zakończona (wykoana/odwołana)
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Appointment cancelAnyAppointment(UUID id) {

        Appointment appointment = appointmentFacade.findById(id);
        if (appointment.getStatus().equals(REJECTED))
            throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        if (appointment.getStatus().equals(FINISHED))
            throw AppointmentStatusException.appointmentStatusAlreadyFinished();
        if(Instant.now().isAfter(appointment.getEndDate())) {
            throw new AppointmentCannotBeCancelledAnymoreException();
        }

        appointment.setStatus(REJECTED);
        appointmentFacade.edit(appointment);

        return appointment;
    }

    /**
     * Metoda pozwalająca na odwołanie własnej wizyty, wywoływana z poziomu serwisu.
     * Może ją wykonać tylko konto z poziomem dostępu klienta/specjalisty
     *
     * @param id identyfikator wizyty, która ma zostać odwołana
     * @return Wizyta, która została odwołana
     * @throws AppointmentStatusException,                   gdy wizyta jest już zakończona (wykonana/odwołana)
     * @throws AppointmentDoesNotBelongToYouException,       gdy wizyta nie należy do Ciebie
     * @throws AppointmentCannotBeCancelledAnymoreException, gdy wizyta nie może zostać odwołana
     */
    @Override
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    public Appointment cancelOwnAppointment(UUID id) {
        Account thisAccount = authContext.getCurrentUser();
        Appointment appointment = appointmentFacade.findById(id);

        // sprawdzenie czy wizyta nie jest już zakończona
        if (appointment.getStatus().equals(REJECTED))
            throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        if (appointment.getStatus().equals(FINISHED))
            throw AppointmentStatusException.appointmentStatusAlreadyFinished();

        // sprawdzenie czy wizyta należy do tego konta
        if (!(
                appointment.getClient().getId().equals(thisAccount.getId())
                        || appointment.getSpecialist().getId().equals(thisAccount.getId())
        ))
            throw new AppointmentDoesNotBelongToYouException();

        // sprawdzenie czy wizytę można anulować (można maksymalnie dzień wcześniej)
        LocalDate today = LocalDate.now();
        LocalDate appointmentDate = LocalDate.ofInstant(appointment.getStartDate(), ZoneId.systemDefault());
        if (today.getDayOfYear() >= appointmentDate.getDayOfYear())
            throw new AppointmentCannotBeCancelledAnymoreException();

        appointment.setStatus(REJECTED);
        appointmentFacade.edit(appointment);

        return appointment;
    }

    /**
     * Metoda tworząca nowy wszczep
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
     * Serwis odpowiadający za archiwizację wszczepu - MOP. 2
     *
     * @param id - uuid archiwizowanego wszczepu
     * @return tmp - zarchiwizowany Implant
     * @throws ImplantStatusException jeżeli archiwizowany wszczep jest już zarchiwizowany
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
     * Metoda zwracająca liste wszczepów
     *
     * @param page     numer strony
     * @param pageSize ilość pozycji na stronie na stronie
     * @param phrase   szukana fraza
     * @param archived określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
     * @throws InvalidParametersException jeśli podano nieprawidłowe parametry
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
     * Metoda zapewniająca możliwość oznaczenia wizyty jako zakończonej
     *
     * @param id    identyfikator wizyty
     * @param login login specjalisty oznaczającego wizytę jako zakończoną
     * @return wizyta oznaczona jako zakończona
     * @throws AppointmentFinishAttemptByInvalidSpecialistException gdy specjalista próbuje zakończyć wizytę inną niż własna
     * @throws AppointmentStatusException                           gdy wizyta jest już odwołana bądź oznaczona jako zakończona
     * @throws AppointmentFinishAttemptBeforeEndDateException       gdy specjalista próbuje oznaczyc wizytę jako zakończoną przed datą zakończenia
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
     * Metoda tworząca recenzję wszczepu oraz zwracająca nowo utworzoną recenzję.
     * Recenzja nie może być utworzona, gdy wszczep nie został jeszcze wmontowany.
     *
     * @param review - Recenzja wszczepu
     * @return Nowo utworzona recenzja wszczepu
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public ImplantReview createReview(ImplantReview review) {
        // TODO: stworzyć fasadę z named query które będzie szukać po loginie i id wszczepu
        //  przeszukiwanie w ten sposób jest BARDZO zasobożerne przy większej ilości recenzji
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
     * Metoda zwracająca liste wizyt
     *
     * @param page     numer aktualnie przeglądanej strony
     * @param pageSize ilość rekordów na danej stronie
     * @param phrase   wyszukiwana fraza
     * @return Lista wizyt zgodnych z parametrami wyszukiwania
     * @throws InvalidParametersException w przypadku podania nieprawidłowych parametrów
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
    public Appointment findVisit(UUID uuid, String clientLogin){
        Account account = accountFacade.findByLogin(clientLogin);
        Appointment appointment = appointmentFacade.findById(uuid);
        if (!account.isInRole(Roles.ADMINISTRATOR)) {
            if(!(appointment.getClient().getLogin().equals(clientLogin)
                    || appointment.getSpecialist().getLogin().equals(clientLogin))) {
                throw new UserNotPartOfAppointment();
            }
            return appointment;
        }
        return appointment;
    }

    /**
     * Metoda zwracająca edytowaną wizytę
     *
     * @param id     id wizyty
     * @param update wartości które mają zostać zaktualizowane
     * @param login  nazwa uzytkownika który bierze udział w wizycie
     * @return Edytowana wizyta
     * @throws UserNotPartOfAppointment   w przypadku gdy użytkownik edytuje nie swoja wizytę
     * @throws AppointmentStatusException w przypadku gdy użytkownik chce edytować zakończoną lub odrzuconą wizytę
     */
    @Override
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    public Appointment editOwnAppointment(UUID id, Appointment update,String login){
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
        // sprawdzenie czy mozna edytować (musi być więcej niż jeden dzień)
        LocalDate today = LocalDate.now();
        LocalDate appointmentDate = LocalDate.ofInstant(appointmentFromDb.getStartDate(), ZoneId.systemDefault());
        if (today.getDayOfYear() >= appointmentDate.getDayOfYear())
            throw new AppointmentCannotBeChangedAnymoreException();

        if(!update.getStartDate().equals(appointmentFromDb.getStartDate())){
        Instant endDate = update.getStartDate().plus(appointmentFromDb.getImplantDuration());
        checkDateAvailabilityForAppointment(appointmentFromDb.getSpecialist().getId(),update.getStartDate(),endDate);
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
     * Metoda zwracająca liste specialistów - MOP.6
     * dostęp dla wszytskich
     *
     * @param page     - numer strony (int)
     * @param pageSize - ilość rekordów na stronie (int)
     * @param phrase   - szukana fraza (String)
     * @return - lista specialistów (PaginationData)
     * @throws InvalidParametersException przy podaniu błędnych parametrów
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
        if(Instant.now().isAfter(appointmentFromDb.getEndDate())) {
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
     * Metoda usuwająca recenzję wszczepu
     *
     * @param id Identyfikator recenzji wszczepu, która ma zostać usunięta
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
     * Metoda tworząca nową wizytę
     *
     * @param clientLogin  - login klienta
     * @param specialistId - identyfikator specjalisty
     * @param implantId    - identyfikator wszczepu
     * @param startDate    - data rozpoczęcia wizyty
     * @return nowa wizyta
     * @throws ImproperAccessLevelException - w przypadku próby przypisanie użytkownika do złej roli w wizycie
     * @throws CantInstallArchivedImplant   - w przypadku kiedy wszczep jest zarchiwizowany
     * @throws StartDateIsInPast            - w przypadku gdy podana data jest datą z przeszłości
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public Appointment createAppointment(String clientLogin, UUID specialistId, UUID implantId, Instant startDate) {
        Appointment appointment = new Appointment();

        // weryfikacja czy klient posiada rolę CLIENT (technicznie nie ma sensu, ale lepiej, żeby było)
        Account client = accountFacade.findByLogin(clientLogin);
        if (!client.isInRole(Roles.CLIENT)) {
            throw ImproperAccessLevelException.accountNotClient();
        }
        appointment.setClient(accountFacade.findByLogin(clientLogin));

        // weryfikacja czy specjalista posiada rolę SPECIALIST
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

        // wryfikacja czy data rozpoczęcia wizyty jest późniejsza niż data aktualna
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
     * MOP.9 - Zarezerwuj wizytę, dostępność specjalisty
     *
     * @param specialistId - id specjalisty
     * @param month        - miesiąc
     * @return lista dostępności
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public List<Instant> getSpecialistAvailabilityInMonth(UUID specialistId, Instant month, Duration duration) {
        // wyciągnięcie ilości dni w danym miesiącu (trzeba tak, bo trzeba pamiętać, że każdy miesiąc ma inną ilość dni)
        // oraz istnieją lata przestępne
        int daysInMonth = YearMonth.from(month.atZone(ZoneId.systemDefault())).lengthOfMonth();
        // wyciągnięcie pierwszego dnia miesiąca
        LocalDate startLocalDate = YearMonth.from(month.atZone(ZoneId.systemDefault())).atDay(1);
        Instant startDate = startLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Instant> availableDates = new ArrayList<>();

        // Założenie jest takie, że wszystkie dni miesiąca, są pracujące.
        // Przeszukiwane są godziny od 8:00 do 15:00 każdego dnia
        // reguła biznesowa: jeżeli zabieg zaczyna się przed 16:00, to może trwać nawet dłużej niż do 16:00
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
     * Metoda zwracająca listę wszystkich recenzji dla danego wszczepu
     *
     * @param page     Aktualny numer strony
     * @param pageSize Ilość recenzji na pojedynczej stronie
     * @param id       Identyfikator wszczepu
     * @return Lista recenzji dla wszczepu
     * @throws InvalidParametersException przy podaniu błędnych parametrów
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
     * Metoda weryfikująca, czy specjalista ma czas na wizytę w danym terminie
     *
     * @param specialistId - identyfikator specjalisty
     * @param startDate    - data rozpoczęcia wizyty
     * @param endDate      - data zakończenia wizyty
     * @throws SpecialistHasNoTimeException w przypadku, gdy specjalista nie ma czasu na wizytę (appBase)
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

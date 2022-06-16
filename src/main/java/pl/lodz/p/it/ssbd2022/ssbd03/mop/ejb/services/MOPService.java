package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
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
 import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.*;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentFinishAttemptBeforeEndDateException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentFinishAttemptByInvalidSpecialistException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentNotFinishedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantArchivedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review.ClientRemovesOtherReviewsException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AppointmentFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AppointmentFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantReviewFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.Date;
import java.util.UUID;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.FINISHED;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.REJECTED;

import java.time.Instant;
import java.util.UUID;

import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AppointmentFacade;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.FINISHED;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.REJECTED;

import java.util.UUID;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MOPService extends AbstractService implements MOPServiceInterface, SessionSynchronization {

    protected static final Logger LOGGER = Logger.getGlobal();

    @Inject
    AppointmentFacade appointmentFacade;

    @Inject
    private ImplantFacade implantFacade;

    @Inject
    private ImplantReviewFacade implantReviewFacade;

    @Inject
    private AccountFacade accountFacade;


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
    public Appointment cancelAppointment(UUID id) {
        Appointment appointment = appointmentFacade.findById(id);
        if (appointment.getStatus().equals(REJECTED))
            throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        if (appointment.getStatus().equals(FINISHED))
            throw AppointmentStatusException.appointmentStatusAlreadyFinished();

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
    public Implant editImplant(UUID uuid, Implant implant){
        Implant implantFromDB = implantFacade.findByUUID(uuid);

        if(implantFromDB.isArchived()){
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
    @PermitAll
    public PaginationData findImplants(int page, int pageSize, String phrase, boolean archived) {
        if (page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return implantFacade.findInRangeWithPhrase(page, pageSize, phrase, archived);
    }

    /**
     * Metoda zapewniająca możliwość oznaczenia wizyty jako zakończonej
     * @param id identyfikator wizyty
     * @param login login specjalisty oznaczającego wizytę jako zakończoną
     * @return wizyta oznaczona jako zakończona
     * @throws AppointmentFinishAttemptByInvalidSpecialistException gdy specjalista próbuje zakończyć wizytę inną niż własna
     * @throws AppointmentStatusException gdy wizyta jest już odwołana bądź oznaczona jako zakończona
     * @throws AppointmentFinishAttemptBeforeEndDateException gdy specjalista próbuje oznaczyc wizytę jako zakończoną przed datą zakończenia
     */
    @RolesAllowed(Roles.SPECIALIST)
    @Override
    public Appointment finishAppointment(UUID id, String login) {
        Appointment appointment = appointmentFacade.findById(id);
        if (!appointment.getSpecialist().getLogin().equals(login)) throw new AppointmentFinishAttemptByInvalidSpecialistException();
        if (appointment.getStatus().equals(REJECTED)) throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        if (appointment.getStatus().equals(FINISHED)) throw AppointmentStatusException.appointmentStatusAlreadyFinished();
        if (appointment.getEndDate().isAfter(Instant.now())) throw new AppointmentFinishAttemptBeforeEndDateException();

        appointment.setStatus(FINISHED);
        appointmentFacade.edit(appointment);
        return appointmentFacade.findById(appointment.getId());
    }

    @Override
    @PermitAll
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
    @PermitAll
    public PaginationData findVisits(int page, int pageSize, String phrase) {
        if (page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return appointmentFacade.findInRangeWithPhrase(page, pageSize, phrase);
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Appointment editAppointmentByAdministrator(UUID uuid, Appointment appointment) {
        Appointment appointmentFromDb = appointmentFacade.findById(uuid);
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
    public void deleteReview(UUID id, String login) {

        ImplantReview review = implantReviewFacade.findByUUID(id);
        Account account = accountFacade.findByLogin(login);
        boolean isAdmin = account.getAccessLevelCollection()
                .stream()
                .anyMatch(accessLevel -> accessLevel.getLevel().equals(Roles.ADMINISTRATOR));

        if(!review.getClient().getLogin().equals(login) && !isAdmin) {
            throw new ClientRemovesOtherReviewsException();
        }
        implantReviewFacade.remove(review);
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
     * @throws CantInstallArchivedImplant - w przypadku kiedy wszczep jest zarchiwizowany
     * @throws StartDateIsInPast - w przypadku gdy podana data jest datą z przeszłości
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
     * Metoda weryfikująca, czy specjalista ma czas na wizytę w danym terminie
     * @param specialistId - identyfikator specjalisty
     * @param startDate    - data rozpoczęcia wizyty
     * @param endDate      - data zakończenia wizyty
     * @throws SpecialistHasNoTimeException w przypadku, gdy specjalista nie ma czasu na wizytę (appBase)
     */
    @TransactionAttribute(TransactionAttributeType.MANDATORY)
    private void checkDateAvailabilityForAppointment(UUID specialistId, Instant startDate, Instant endDate) {
        PaginationData appointments = appointmentFacade.findSpecialistAppointmentsInGivenPeriod(specialistId, startDate, endDate, 1, 1);
        if (appointments.getData().size() > 0) {
            throw new SpecialistHasNoTimeException();
        }
    }
}

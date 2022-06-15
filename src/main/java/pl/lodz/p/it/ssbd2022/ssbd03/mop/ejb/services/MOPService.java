package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.account.AccountStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentNotFinishedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AppointmentFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantReviewFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

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


    /**
     * Metoda pozwalająca na odwołanie dowolnej wizyty, wywoływana z poziomu serwisu.
     * Może ją wykonać tylko konto z poziomem dostępu administratora
     *
     * @param id identyfikator wizyty, która ma zostać odwołana
     * @return Wizyta, która została odwołana
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Appointment cancelAppointment(UUID id) {
        Appointment appointment = appointmentFacade.findById(id);
        if (appointment.getStatus().equals(REJECTED)) throw AppointmentStatusException.appointmentStatusAlreadyCancelled();
        if (appointment.getStatus().equals(FINISHED)) throw AppointmentStatusException.appointmentStatusAlreadyFinished();

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
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Override
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

    /**
     * Metoda zwracająca liste wszczepów
     * @param page numer strony
     * @param pageSize  ilość pozycji na stronie na stronie
     * @param phrase szukana fraza
     * @param archived określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
     * @throws InvalidParametersException jeśli podano nieprawidłowe parametry
     */
    @Override
    @PermitAll
    public PaginationData findImplants(int page, int pageSize, String phrase, boolean archived) {
        if(page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return implantFacade.findInRangeWithPhrase(page, pageSize, phrase, archived);
    }

    @Override
    @PermitAll
    public Implant findImplantByUuid(UUID uuid){
        return implantFacade.findByUUID(uuid);
    }

    /**
     * Metoda tworząca recenzję wszczepu oraz zwracająca nowo utworzoną recenzję.
     * Recenzja nie może być utworzona, gdy wszczep nie został jeszcze wmontowany.
     * @param review - Recenzja wszczepu
     * @return Nowo utworzona recenzja wszczepu
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public ImplantReview createReview(ImplantReview review) {
        Appointment clientAppointment = appointmentFacade.findByClientLogin(review.getClient().getLogin())
                .stream()
                .filter(appointment -> appointment.getImplant().getId().equals(review.getImplant().getId()))
                .findFirst()
                .orElseThrow(AppointmentNotFoundException::new);

        if(!clientAppointment.getStatus().equals(Status.FINISHED)) {
            throw new AppointmentNotFinishedException();
        }

        implantReviewFacade.create(review);
        return implantReviewFacade.findByUUID(review.getId());
    }

    /**
     * Metoda zwracająca liste wizyt
     *
     * @param page numer aktualnie przeglądanej strony
     * @param pageSize  ilość rekordów na danej stronie
     * @param phrase wyszukiwana fraza
     * @return  Lista wizyt zgodnych z parametrami wyszukiwania
     * @throws InvalidParametersException w przypadku podania nieprawidłowych parametrów
     */
    @Override
    @PermitAll
    public PaginationData findVisits(int page, int pageSize, String phrase) {
        if(page == 0 || pageSize == 0) {
            throw new InvalidParametersException();
        }
        return appointmentFacade.findInRangeWithPhrase(page, pageSize, phrase);
    }

    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Appointment editAppointmentByAdministrator(UUID uuid, Appointment appointment) {
        Appointment appointmentFromDb = appointmentFacade.findById(uuid);
        appointmentFromDb.setDescription(appointment.getDescription());
        if(appointmentFromDb.getStatus() == Status.ACCEPTED) {
            appointmentFromDb.setStatus(appointment.getStatus());
        }
        appointmentFacade.edit(appointmentFromDb);
        return appointmentFromDb;
    }
}

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
 import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentFinishAttemptBeforeEndDateException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentFinishAttemptByInvalidSpecialistException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AppointmentFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.Date;
import java.util.UUID;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.FINISHED;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.REJECTED;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MOPService extends AbstractService implements MOPServiceInterface, SessionSynchronization {

    @Inject
    private ImplantFacade implantFacade;

    @Inject
    private AppointmentFacade appointmentFacade;

    /**
     * Metoda tworząca nowy wszczep
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
        if (appointment.getEndDate().after(new Date())) throw new AppointmentFinishAttemptBeforeEndDateException();

        appointment.setStatus(FINISHED);
        appointmentFacade.edit(appointment);
        return appointmentFacade.findById(appointment.getId());
    }

}

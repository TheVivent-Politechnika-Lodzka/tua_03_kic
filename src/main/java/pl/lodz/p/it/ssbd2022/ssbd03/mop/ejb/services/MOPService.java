package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractService;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.appointment.AppointmentStatusException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AppointmentFacade;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.FINISHED;
import static pl.lodz.p.it.ssbd2022.ssbd03.entities.Status.REJECTED;

@Stateful
@DenyAll
@Interceptors(TrackerInterceptor.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class MOPService extends AbstractService implements MOPServiceInterface, SessionSynchronization {

    protected static final Logger LOGGER = Logger.getGlobal();

    @Inject
    AppointmentFacade appointmentFacade;

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

}

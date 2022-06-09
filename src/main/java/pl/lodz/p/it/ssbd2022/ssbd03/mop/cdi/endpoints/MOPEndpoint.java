package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AppointmentMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPService;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.util.List;

@RequestScoped
@DenyAll
@Path("/mop")
public class MOPEndpoint implements MOPEndpointInterface{

    @Inject
    MOPServiceInterface mopService;

    @Inject
    AppointmentMapper appointmentMapper;

    @Inject
    private Tagger tagger;

    /**
     * MOP.13 Odwołaj dowolną wizytę
     * Metodę może wykonać tylko konto z poziomem dostępu administratora.
     *
     * @param id Identyfikator wizyty, która ma zostać odwołana
     * @return odpowiedź HTTP
     * @throws MethodNotImplementedException w momencie, gdy metoda jest niezaimplementowana
     */
    @Override
    public Response cancelAnyVisit(String id) {
        tagger.verifyTag();
        Appointment cancelledAppointment;

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            cancelledAppointment = mopService.cancelAppointment(id);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(cancelledAppointment);

        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }


}

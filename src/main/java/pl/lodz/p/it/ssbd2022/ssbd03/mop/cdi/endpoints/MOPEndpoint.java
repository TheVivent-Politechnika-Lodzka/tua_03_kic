package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AppointmentMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantReviewMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;
import java.util.UUID;

@RequestScoped
@DenyAll
@Path("/mop")
public class MOPEndpoint implements MOPEndpointInterface {

    @Inject
    MOPServiceInterface mopServiceInterface;

    @Inject
    AppointmentMapper appointmentMapper;

    @Inject
    private ImplantMapper implantMapper;

    @Inject
    private Tagger tagger;

    @Inject
    private ImplantReviewMapper implantReviewMapper;

    /**
     * MOP.1 - Dodaj nowy wszczep
     *
     * @param createImplantDto - dane nowego wszczepu
     * @return odpowiedź zawierająca status http
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response createImplant(CreateImplantDto createImplantDto) {

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        Implant implant = implantMapper.createImplantFromDto(createImplantDto);
        Implant createdImplant;
        do {
            createdImplant = mopServiceInterface.createImplant(implant);
            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }
        ImplantDto implantDto = implantMapper.createImplantDtoFromImplant(createdImplant);

        return Response.ok(implantDto).build();
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

        Implant archiveImplant;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;

        do {
            archiveImplant = mopServiceInterface.archiveImplant(id);
            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        ImplantDto imp = implantMapper.createImplantDtoFromImplant(archiveImplant);

        return Response.ok(imp).tag(tagger.tag(imp)).build();
    }

    //MOP.4 - Przegladaj szczegoły wszczepu
    @Override
    public Response getImplant(UUID id) {
        Implant implant;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            implant = mopServiceInterface.findImplantByUuid(id);

            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && TXCounter-- > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

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
    @PermitAll
    @Override
    public Response listImplants(int page, int size, String phrase, boolean archived) {
        PaginationData paginationData;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            paginationData = mopServiceInterface.findImplants(page, size, phrase, archived);
            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && TXCounter-- > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        List<Implant> implants = paginationData.getData();
        List<ImplantListElementDto> implantsDto = implantMapper.getListFromImplantListElementDtoFromImplant(implants);
        paginationData.setData(implantsDto);
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
    @PermitAll
    @Override
    public Response listVisits(int page, int size, String phrase) {
        PaginationData paginationData;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            paginationData = mopServiceInterface.findVisits(page, size, phrase);
            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && TXCounter-- > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        List<Appointment> appointments = paginationData.getData();
        List<AppointmentListElementDto> appointmentDtos = appointmentMapper.appointmentListElementDtoList(appointments);
        paginationData.setData(appointmentDtos);
        return Response.ok().entity(paginationData).build();
    }

    /**
     * MOP.11 - Edytuj dowolną wizytę
     *
     * @param id                 id konkretnej wizyty
     * @param appointmentEditDto obiekt dto edytowanej wizyty
     * @return odpowiedz HTTP
     */
    @Override
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Response editVisit(UUID id, AppointmentEditDto appointmentEditDto) {
        tagger.verifyTag(appointmentEditDto);

        Appointment update = appointmentMapper.createAppointmentFromEditDto(appointmentEditDto);
        Appointment editedAppointment;

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            editedAppointment = mopServiceInterface.editAppointment(id, update);
            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        AppointmentEditDto app = appointmentMapper.createEditDtoFromAppointment(editedAppointment);

        return Response.ok(app).tag(tagger.tag(app)).build();
    }


    /**
     * MOP.13 Odwołaj dowolną wizytę
     * Metodę może wykonać tylko konto z poziomem dostępu administratora.
     *
     * @param id Identyfikator wizyty, która ma zostać odwołana
     * @return odpowiedź HTTP
     */
    @Override
    public Response cancelAnyVisit(UUID id) {
        tagger.verifyTag();
        Appointment cancelledAppointment;

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            cancelledAppointment = mopServiceInterface.cancelAppointment(id);
            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(cancelledAppointment);

        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }

    /**
     * MOK.15 - Dodaj recenzję wszczepu
     *
     * @param createImplantReviewDto - Nowo napisana recenzja
     * @return nowo utworzona recenzja
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response addImplantsReview(CreateImplantReviewDto createImplantReviewDto) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        ImplantReview implantReview = implantReviewMapper.createImplantReviewFromDto(createImplantReviewDto);
        ImplantReview createdReview;
        do {
            createdReview = mopServiceInterface.createReview(implantReview);
            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        ImplantReviewDto createdReviewDto = implantReviewMapper.implantReviewDtofromImplantReview(createdReview);
        return Response.ok().entity(createdReviewDto).build();
    }


}

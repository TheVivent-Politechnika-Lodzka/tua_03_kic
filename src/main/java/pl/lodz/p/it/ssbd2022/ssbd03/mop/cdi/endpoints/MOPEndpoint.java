package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.MethodNotImplementedException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.InAppOptimisticLockException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.AppointmentMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.AppointmentDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantListElementDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantReviewMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.*;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.security.AuthContext;

import java.util.UUID;

import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.time.Instant;
import java.util.UUID;

import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

@RequestScoped
@DenyAll
@Path("/mop")
public class MOPEndpoint implements MOPEndpointInterface {

    @Inject
    private MOPServiceInterface mopService;

    @Inject
    AuthContext authContext;

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

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        Implant implant = implantMapper.createImplantFromDto(createImplantDto);
        Implant createdImplant;
        do {
            createdImplant = mopService.createImplant(implant);
            commitedTX = mopService.isLastTransactionCommited();
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
            archiveImplant = mopService.archiveImplant(id);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        ImplantDto imp = implantMapper.createImplantDtoFromImplant(archiveImplant);

        return Response.ok(imp).tag(tagger.tag(imp)).build();
    }

    //MOP.3 -Edytuj wszczep
    public Response editImplant(UUID id, ImplantDto implantDto) {
        tagger.verifyTag(implantDto);

        Implant implant;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            implant = mopService.editImplant(id, implantMapper.createImplantFromImplantDto(implantDto));
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        ImplantDto updatedImplant = implantMapper.createImplantDtoFromImplant(implant);

        return Response.ok(updatedImplant).tag(tagger.tag(updatedImplant)).build();
    }

    //MOP.4 - Przegladaj szczegoły wszczepu
    @Override
    public Response getImplant(UUID id) {
        Implant implant;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            implant = mopService.findImplantByUuid(id);

            commitedTX = mopService.isLastTransactionCommited();
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
            paginationData = mopService.findImplants(page, size, phrase, archived);
            commitedTX = mopService.isLastTransactionCommited();
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
     * MOP.6 - Przeglądaj listę specialistów
     * dostęp posiadają wszyscy użytkownicy serwisu włącznie z nieuwierzytelnionymi
     *
     * @param page   - numer strony (int)
     * @param size   - ilość specialistó wyświetlanych na jednej stronie (int)
     * @param phrase - szukana fraza specialisty (String)
     * @return zwraca odpowiedz zawierającą listę specialistów
     * @throws TransactionException błąd transakcji
     */
    @PermitAll
    @Override
    public Response listSpecialists(int page, int size, String phrase) {
        PaginationData paginationData;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            paginationData = mopService.findSpecialists(page, size, phrase);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && TXCounter-- > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

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
    public Response listVisits(int page, int size, String phrase) {
        PaginationData paginationData;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            paginationData = mopService.findVisits(page, size, phrase);
            commitedTX = mopService.isLastTransactionCommited();
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
     * MOP.8 - Przeglądaj swoje wizyty
     *
     * @param page numer aktualnie przeglądanej strony
     * @param size ilość rekordów na danej stronie
     * @return lista wizyt
     * @throws TransactionException w przypadku braku zatwierdzenia transakcji
     */
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    @Override
    public Response listMyVisits(int page, int size) {
        PaginationData paginationData;
        String login = authContext.getCurrentUserLogin();
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            paginationData = mopService.findVisitsByLogin(page, size, login);
            commitedTX = mopService.isLastTransactionCommited();
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
     * MOP.9 - Zarezerwuj wizytę
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

        Appointment createdAppointment;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            createdAppointment = mopService.createAppointment(
                    clientLogin,
                    specialistId,
                    implantId,
                    startDate
            );
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && TXCounter-- > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }
        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(createdAppointment);

        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }
    /**
     * MOP.10 - Edytuj swoją wizytę
     *
     * @param id                 id konkretnej wizyty
     * @param appointmentOwnEditDto obiekt dto edycji naniesionych do wizyty
     * @return odpowiedz HTTP
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response editOwnVisit(UUID id, AppointmentOwnEditDto appointmentOwnEditDto) {
        tagger.verifyTag(appointmentOwnEditDto);
        String login = authContext.getCurrentUserLogin();
        Appointment update = appointmentMapper.createAppointmentFromAppointmentOwnEditDto(appointmentOwnEditDto);
        Appointment editedAppointment;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            editedAppointment = mopService.editOwnAppointment(id, update, login);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);
        if (!commitedTX) {
            throw new TransactionException();
        }
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
    @RolesAllowed(Roles.ADMINISTRATOR)
    public Response editVisit(UUID id, AppointmentEditDto appointmentEditDto) {
        tagger.verifyTag(appointmentEditDto);

        Appointment update = appointmentMapper.createAppointmentFromEditDto(appointmentEditDto);
        Appointment editedAppointment;

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            editedAppointment = mopService.editAppointmentByAdministrator(id, update);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        AppointmentEditDto app = appointmentMapper.createEditDtoFromAppointment(editedAppointment);

        return Response.ok(app).tag(tagger.tag(app)).build();
    }

    /** MOP.12 - Odwołaj swoją wizytę
     * Endpoint pozwalający odwołać wizytę (REJECTED)
     *
     * @param id - id wizyty
     * @return status HTTP oraz zmodyfikowana wizyta
     * @throws TransactionException, gdy transakcja się nie powiedzie
     */
    @Override
    public Response cancelOwnVisit(UUID id) {
        tagger.verifyTag();
        Appointment cancelledAppointment;

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            cancelledAppointment = mopService.cancelOwnAppointment(id);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

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
    public Response cancelAnyVisit(UUID id) {
        tagger.verifyTag();
        Appointment cancelledAppointment;

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            cancelledAppointment = mopService.cancelAnyAppointment(id);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

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
    public Response finishVisit(UUID id) {
        tagger.verifyTag();

        String login = authContext.getCurrentUserLogin();
        Appointment finishedAppointment = null;

        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX = false;

        do {
            try {
                finishedAppointment = mopService.finishAppointment(id, login);
                commitedTX = mopService.isLastTransactionCommited();
            } catch (InAppOptimisticLockException ex) {
                if (TXCounter - 1 <= 0) {
                    throw ex;
                }
            }
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX || finishedAppointment == null) {
            throw new TransactionException();
        }

        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(finishedAppointment);
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
            createdReview = mopService.createReview(implantReview);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

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
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;

        String login = authContext.getCurrentUserLogin();
        do {
            mopService.deleteReview(id, login);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok().build();
    }
    @Override
    public Response getVisitDetails (UUID uuid){
        String login = authContext.getCurrentUserLogin();
        Appointment appointment;
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        do {
            appointment = mopService.findVisit(uuid, login);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }
        AppointmentDto appointmentDto = appointmentMapper.createAppointmentDtoFromAppointment(appointment);
        return Response.ok(appointmentDto).tag(tagger.tag(appointmentDto)).build();
    }

    /**
     * MOP.18 - Wyświetl recenzje dla danego wszczepu
     * @param size Ilość recenzji do wyświetlenia na jednej stronie
     * @param page Numer strony
     * @param id Identyfikator wszczepu
     * @return Lista recenzji wszczepu
     * @throws TransactionException jeśli transakcja nie została zatwierdzona
     */
    @Override
    public Response getAllImplantReviews(int page, int size, UUID id) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        PaginationData paginationData;

        do {
            paginationData = mopService.getAllImplantReviews(page, size, id);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }
        List<ImplantReview> implantReviews = paginationData.getData();
        List<ImplantReviewDto> implantReviewDtos = implantReviewMapper.implantReviewDtoListfromImplantReviewList(implantReviews);
        paginationData.setData(implantReviewDtos);
        return Response.ok().entity(paginationData).build();
    }
}

package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantReviewMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantListElementDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

@RequestScoped
@DenyAll
@Path("/mop")
public class MOPEndpoint implements MOPEndpointInterface{

    @Inject
    MOPServiceInterface mopService;

    @Inject
    private ImplantMapper implantMapper;

    @Inject
    private ImplantReviewMapper implantReviewMapper;

    /**
     * MOP.1 - Dodaj nowy wszczep
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

        return Response.ok(createdImplant).build();
    }

    /**
     * MOK.5 - Przeglądaj listę wszczepów
     * @param page numer strony
     * @param size ilość pozycji na stronie
     * @param phrase szukana fraza
     * @param archived określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
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
     * MOK.15 - Dodaj recenzję wszczepu
     *
     */
    @Override
    public Response addImplantsReview(CreateReviewDto createReviewDto) {
        int TXCounter = Config.MAX_TX_RETRIES;
        boolean commitedTX;
        ImplantReview implantReview = implantReviewMapper.createImplantReviewFromDto(createReviewDto);
        ImplantReview createdReview;
        do {
            createdReview = mopService.createReview(implantReview);
            commitedTX = mopService.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }
        return Response.ok().entity(createdReview).build();
    }





}

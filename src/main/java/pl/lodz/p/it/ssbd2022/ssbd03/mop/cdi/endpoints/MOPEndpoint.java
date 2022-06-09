package pl.lodz.p.it.ssbd2022.ssbd03.mop.cdi.endpoints;

import jakarta.annotation.security.DenyAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Config;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.TransactionException;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.ImplantMapper;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.util.UUID;


@RequestScoped
@DenyAll
@Path("/mop")
public class MOPEndpoint implements MOPEndpointInterface {

    @Inject
    MOPServiceInterface mopServiceInterface;

    @Inject
    private ImplantMapper implantMapper;

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
            createdImplant = mopServiceInterface.createImplant(implant);
            commitedTX = mopServiceInterface.isLastTransactionCommited();
        } while (!commitedTX && --TXCounter > 0);

        if (!commitedTX) {
            throw new TransactionException();
        }

        return Response.ok(createdImplant).build();
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

}

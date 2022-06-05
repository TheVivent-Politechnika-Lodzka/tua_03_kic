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
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.services.MOPServiceInterface;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

@RequestScoped
@DenyAll
@Path("/mop")
public class MOPEndpoint implements MOPEndpointInterface{

    @Inject
    MOPServiceInterface mopService;

    @Inject
    private ImplantMapper implantMapper;


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
    
}

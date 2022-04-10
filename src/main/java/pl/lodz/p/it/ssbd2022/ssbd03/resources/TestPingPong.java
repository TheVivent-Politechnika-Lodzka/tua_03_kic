package pl.lodz.p.it.ssbd2022.ssbd03.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.model.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Path("ping")
public class TestPingPong {

    @Inject
    private AccountFacade accountFacade;
    @Inject
    private HashAlgorithm hashAlgorithm;
    @GET
    @Transactional(Transactional.TxType.REQUIRED)
    public Response ping() {

        Account account = new Account();
        account.setLogin("test"+String.valueOf(System.currentTimeMillis()));
        account.setPassword(hashAlgorithm.generate("test".toCharArray()));
        account.setEmail("test@test.ts");
        account.setFirstName("test");
        account.setSurname("test");
        account.setActive(true);
        account.setConfirmed(true);
        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setAccount(account);
        dataAdministrator.setPhoneNumber("123-456-789");
        account.getAccessLevelCollection().add(dataAdministrator);
        DataClient dataClient = new DataClient();
        dataClient.setAccount(account);
        dataClient.setPesel("12345678901");
        account.getAccessLevelCollection().add(dataClient);

        accountFacade.create(account);

        return Response.ok("pong").build();
    }

}


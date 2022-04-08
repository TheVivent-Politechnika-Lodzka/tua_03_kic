package pl.lodz.p.it.ssbd2022.ssbd03.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import pl.lodz.p.it.ssbd2022.ssbd03.model.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.model.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.model.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.mok.ejb.facades.AccountFacade;

@Path("ping")
public class TestPingPong {

    @Inject
    private AccountFacade accountFacade;

    @GET
    @Transactional(Transactional.TxType.REQUIRED)
    public Response ping() {

        Account account = new Account();
        account.setLogin("test"+String.valueOf(System.currentTimeMillis()));
        account.setPassword("test");
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


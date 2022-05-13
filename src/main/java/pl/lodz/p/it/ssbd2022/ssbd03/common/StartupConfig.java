package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

@Startup
@Singleton
public class StartupConfig {

    @PersistenceContext(unitName = "ssbd03adminPU")
    private EntityManager em;

    @Inject
    private HashAlgorithm hashAlgorithm;

    @PostConstruct
    public void init() {
        createAdmin();
        createSpecialistAdmin();
        createClientAdmin();
        em.flush();
    }

    private void createAdmin() {
        Account admin = new Account();
        admin.setLogin("administrator");
        admin.setPassword(hashAlgorithm.generate("Password123!".toCharArray()));
        admin.setActive(true);
        admin.setConfirmed(true);
        admin.setFirstName("admin");
        admin.setLastName("administracyjny");
        admin.setEmail("szurySSBD@gmail.com");


        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("000-000-000");
        dataAdministrator.setEmail("administrator@kic.agency");

        admin.addAccessLevel(dataAdministrator);
        em.persist(admin);
    }

    private void createSpecialistAdmin() {
        Account admin = new Account();
        admin.setLogin("specAdmin");
        admin.setPassword(hashAlgorithm.generate("Password123!".toCharArray()));
        admin.setActive(true);
        admin.setConfirmed(true);
        admin.setFirstName("admin");
        admin.setLastName("specjalny");
        admin.setEmail("szurySSBD2@gmail.com");

        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("111-111-111");
        dataAdministrator.setEmail("specadmin@kic.agency");
        DataSpecialist dataSpecialist = new DataSpecialist();
        dataSpecialist.setPhoneNumber("111-111-111");
        dataSpecialist.setEmail("specadmin@kic.agency");

        admin.addAccessLevel(dataAdministrator);
        admin.addAccessLevel(dataSpecialist);
        em.persist(admin);
    }

    private void createClientAdmin() {
        Account admin = new Account();
        admin.setLogin("clientAdmin");
        admin.setPassword(hashAlgorithm.generate("Password123!".toCharArray()));
        admin.setActive(true);
        admin.setConfirmed(true);
        admin.setFirstName("admin");
        admin.setLastName("kliencki");
        admin.setEmail("szurySSBD3@gmail.com");

        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("222-222-222");
        dataAdministrator.setEmail("klientadmin@kic.agency");

        DataClient dataClient = new DataClient();
        dataClient.setPesel("22222222222");
        dataClient.setPhoneNumber("222-222-222");
        dataClient.setEmail("klientadmin@kic.agency");

        admin.addAccessLevel(dataAdministrator);
        admin.addAccessLevel(dataClient);
        em.persist(admin);
    }

}

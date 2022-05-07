package pl.lodz.p.it.ssbd2022.ssbd03;

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
        admin.setSurname("administracyjny");
        admin.setPesel("00000000000");
        admin.setPhoneNumber("000-000-000");
        admin.setEmail("administrator@kic.agency");

        DataAdministrator dataAdministrator = new DataAdministrator();
        DataSpecialist dataSpecialist = new DataSpecialist();
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
        admin.setSurname("specjalny");
        admin.setPesel("11111111111");
        admin.setPhoneNumber("111-111-111");
        admin.setEmail("specadmin@kic.agency");

        DataAdministrator dataAdministrator = new DataAdministrator();
        DataSpecialist dataSpecialist = new DataSpecialist();
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
        admin.setSurname("kliencki");
        admin.setPesel("22222222222");
        admin.setPhoneNumber("222-222-222");
        admin.setEmail("klientadmin@kic.agency");

        DataAdministrator dataAdministrator = new DataAdministrator();
        DataClient dataClient = new DataClient();
        admin.addAccessLevel(dataAdministrator);
        admin.addAccessLevel(dataClient);
        em.persist(admin);
    }

}

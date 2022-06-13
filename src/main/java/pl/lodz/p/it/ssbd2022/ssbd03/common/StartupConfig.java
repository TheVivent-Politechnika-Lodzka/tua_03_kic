package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;
import java.time.Duration;
import java.util.Date;
import java.util.Locale;

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
        createClient();
        createSpecialist();
        createAppointment();
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
        admin.setEmail("szurySSBD3@gmail.com");
        admin.setLanguage(new Locale("pl"));


        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("000000000");
        dataAdministrator.setContactEmail("administrator@kic.agency");

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
        admin.setLanguage(new Locale("en"));

        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("111111111");
        dataAdministrator.setContactEmail("specadmin@kic.agency");
        DataSpecialist dataSpecialist = new DataSpecialist();
        dataSpecialist.setPhoneNumber("111111111");
        dataSpecialist.setContactEmail("specadmin@kic.agency");

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
        admin.setEmail("szurySSBD@gmail.com");
        admin.setLanguage(new Locale("pl"));

        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("222222222");
        dataAdministrator.setContactEmail("klientadmin@kic.agency");

        DataClient dataClient = new DataClient();
        dataClient.setPesel("22222222222");
        dataClient.setPhoneNumber("222222222");

        admin.addAccessLevel(dataAdministrator);
        admin.addAccessLevel(dataClient);
        em.persist(admin);
    }

    public void createClient() {
        Account client = new Account();
        client.setLogin("client");
        client.setPassword(hashAlgorithm.generate("Password123!".toCharArray()));
        client.setActive(true);
        client.setConfirmed(true);
        client.setFirstName("client");
        client.setLastName("klient");
        client.setEmail("szurySSBDclient@gmail.com");
        client.setLanguage(new Locale("pl"));

        DataClient dataClient = new DataClient();
        dataClient.setPesel("11111111111");
        dataClient.setPhoneNumber("111111111");

        client.addAccessLevel(dataClient);
        em.persist(client);
    }

    public void createSpecialist() {
        Account specialist = new Account();
        specialist.setLogin("spec");
        specialist.setPassword(hashAlgorithm.generate("Password123!".toCharArray()));
        specialist.setActive(true);
        specialist.setConfirmed(true);
        specialist.setFirstName("spec");
        specialist.setLastName("specjalista");
        specialist.setEmail("szurySSBDspec@gmail.com");
        specialist.setLanguage(new Locale("pl"));

        DataSpecialist dataSpecialist = new DataSpecialist();
        dataSpecialist.setPhoneNumber("222222222");
        dataSpecialist.setContactEmail("email@email.com");

        specialist.addAccessLevel(dataSpecialist);
        em.persist(specialist);
    }

    public void createAppointment() {

        Account client = em.createNamedQuery("Account.findByLogin", Account.class).setParameter("login", "client").getSingleResult();
        Account specialist = em.createNamedQuery("Account.findByLogin", Account.class).setParameter("login", "spec").getSingleResult();

        Implant implant = new Implant();
        implant.setName("Implant tak fajny ze wszystkich stron");
        implant.setDescription("Na pierwszym planie obrazu widać wzgórze, " +
                "na którym oracz orze ziemie.Ten fragment płótna przyciąga uwagę, " +
                "gdyż wzgórze przedstawione jest w jasnych kolorach. " +
                "Chłop ma na sobie czerwony kubrak przykuwający wzrok na tle " +
                "brązów i zieleni.Na dalszym planie widać pasterza i psa pilnującego " +
                "stado owiec.Oraz rybaka zarzucającego sieć, statek, miasto i " +
                "zachodzące słońce");
        implant.setPrice(100);
        implant.setManufacturer("Manufacturer kox");
        implant.setPopularity(0);
        implant.setDuration(Duration.ofDays(1));

        em.persist(implant);

        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setSpecialist(specialist);
        appointment.setImplant(implant);
        appointment.setStartDate(new Date());
        appointment.setEndDate(new Date());
        appointment.setStatus(Status.FINISHED); // TUTAJ ZMIENIAĆ DO TESTÓW
        appointment.setPrice(100);
        appointment.setDescription("Appointment description");

        em.persist(appointment);
    }
}

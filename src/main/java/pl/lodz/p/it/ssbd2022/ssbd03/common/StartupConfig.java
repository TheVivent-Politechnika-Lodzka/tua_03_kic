package pl.lodz.p.it.ssbd2022.ssbd03.common;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.*;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
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
        createImplant();
        em.flush();

        createAppointment();
        em.flush();

        createImplantReview();
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
        admin.setUrl("https://assets.reedpopcdn.com/cyberpunk-2077-wyciekl-dodatek-headline.jpg/BROK/resize/1920x1920%3E/format/jpg/quality/80/cyberpunk-2077-wyciekl-dodatek-headline.jpg");
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
        admin.setUrl("https://assets.reedpopcdn.com/cyberpunk-2077-wyciekl-dodatek-headline.jpg/BROK/resize/1920x1920%3E/format/jpg/quality/80/cyberpunk-2077-wyciekl-dodatek-headline.jpg");

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
        admin.setUrl("https://assets.reedpopcdn.com/cyberpunk-2077-wyciekl-dodatek-headline.jpg/BROK/resize/1920x1920%3E/format/jpg/quality/80/cyberpunk-2077-wyciekl-dodatek-headline.jpg");

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
        client.setUrl("https://assets.reedpopcdn.com/cyberpunk-2077-wyciekl-dodatek-headline.jpg/BROK/resize/1920x1920%3E/format/jpg/quality/80/cyberpunk-2077-wyciekl-dodatek-headline.jpg");

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
        specialist.setUrl("https://assets.reedpopcdn.com/cyberpunk-2077-wyciekl-dodatek-headline.jpg/BROK/resize/1920x1920%3E/format/jpg/quality/80/cyberpunk-2077-wyciekl-dodatek-headline.jpg");

        DataSpecialist dataSpecialist = new DataSpecialist();
        dataSpecialist.setPhoneNumber("222222222");
        dataSpecialist.setContactEmail("email@email.com");

        specialist.addAccessLevel(dataSpecialist);
        em.persist(specialist);
    }

    public void createImplant() {
        Implant implant = new Implant();
        implant.setName("implant pierwszy");
        implant.setDescription("testowy implant zwiększający siłę przebicia przez ściany amerykańskie (z kartonu) o 10% maksymalnego zdrowia");
        implant.setManufacturer("Janusz Nowak");
        implant.setPrice(1000);
        implant.setPopularity(0);
        implant.setDuration(Duration.between(LocalTime.NOON, LocalTime.MAX));
        implant.setImage("https://assets.reedpopcdn.com/cyberpunk-2077-wyciekl-dodatek-headline.jpg/BROK/resize/1920x1920%3E/format/jpg/quality/80/cyberpunk-2077-wyciekl-dodatek-headline.jpg");
        em.persist(implant);
    }

//    public void createAppointment() {
//
//        Account accountClient = em.createNamedQuery("Account.findByLogin", Account.class)
//                .setParameter("login", "client").getSingleResult();
//        Account accountSpecialist = em.createNamedQuery("Account.findByLogin", Account.class)
//                .setParameter("login", "spec").getSingleResult();
//        List<Implant> implants = em.createNamedQuery("Implant.findAll", Implant.class).getResultList();
//
//        Appointment appointment = new Appointment();
//        Date dateStart = null;
//        try {
//            dateStart = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2021");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Date dateEnd = null;
//        try {
//            dateEnd = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2021");
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        appointment.setStartDate(dateStart);
//        appointment.setEndDate(dateEnd);
//        appointment.setPrice(100);
//        appointment.setStatus(Status.PENDING);
//        appointment.setDescription("Przykladowa wizyta");
//        appointment.setSpecialist(accountSpecialist);
//        appointment.setClient(accountClient);
//        appointment.setImplant(implants.get(0));
//
//        em.persist(appointment);
//    }

    public void createAppointment() {

        Account clientAdmin = em.createNamedQuery("Account.findByLogin", Account.class).setParameter("login", "clientAdmin").getSingleResult(); // zmieniac - testy
        Account specialist = em.createNamedQuery("Account.findByLogin", Account.class).setParameter("login", "spec").getSingleResult();

        Implant implant = new Implant();
        implant.setName("Implant drugi");
        implant.setDescription("""
                Na pierwszym planie obrazu widać wzgórze,
                na którym oracz orze ziemie.Ten fragment płótna przyciąga uwagę,
                gdyż wzgórze przedstawione jest w jasnych kolorach.
                Chłop ma na sobie czerwony kubrak przykuwający wzrok na tle
                brązów i zieleni.Na dalszym planie widać pasterza i psa pilnującego
                stado owiec.Oraz rybaka zarzucającego sieć, statek, miasto i
                zachodzące słońce
                """);
        implant.setPrice(100);
        implant.setManufacturer("Manufacturer kox");
        implant.setPopularity(0);
        implant.setDuration(Duration.ofMinutes(30));
        implant.setImage("https://assets.reedpopcdn.com/cyberpunk-2077-wyciekl-dodatek-headline.jpg/BROK/resize/1920x1920%3E/format/jpg/quality/80/cyberpunk-2077-wyciekl-dodatek-headline.jpg");


        em.persist(implant);

        Appointment appointment = new Appointment();
        appointment.setClient(clientAdmin); // TUTAJ ZMIENIAĆ DO TESTÓW
        appointment.setSpecialist(specialist);
        appointment.setImplant(implant);
        appointment.setStartDate(Instant.now());
        appointment.setEndDate(Instant.now().plusSeconds(10)); // zmieniono do testow
        appointment.setStatus(Status.ACCEPTED); // TUTAJ ZMIENIAĆ DO TESTÓW
        appointment.setPrice(100);
        appointment.setDescription("Appointment description");

        em.persist(appointment);
    }

    public void createImplantReview() {
        ImplantReview review = new ImplantReview();
        review.setReview("Testowy review");
        review.setRating(5);

        Implant implant = new Implant();

        implant.setName("Implant trzeci");
        implant.setDescription("""
                Na pierwszym planie obrazu widać wzgórze,
                na którym oracz orze ziemie.Ten fragment płótna przyciąga uwagę,
                gdyż wzgórze przedstawione jest w jasnych kolorach.
                Chłop ma na sobie czerwony kubrak przykuwający wzrok na tle
                brązów i zieleni.Na dalszym planie widać pasterza i psa pilnującego
                stado owiec.Oraz rybaka zarzucającego sieć, statek, miasto i
                zachodzące słońce
                """);
        implant.setPrice(100);
        implant.setManufacturer("Manufacturer super gut");
        implant.setPopularity(0);
        implant.setDuration(Duration.ofMinutes(120));
        implant.setImage("https://assets.reedpopcdn.com/cyberpunk-2077-wyciekl-dodatek-headline.jpg/BROK/resize/1920x1920%3E/format/jpg/quality/80/cyberpunk-2077-wyciekl-dodatek-headline.jpg");

        em.persist(implant);

        Account client = em.createNamedQuery("Account.findByLogin", Account.class)
                .setParameter("login", "client")
                .getSingleResult();

        review.setClient(client);
        review.setImplant(implant);
        em.persist(review);
    }
}

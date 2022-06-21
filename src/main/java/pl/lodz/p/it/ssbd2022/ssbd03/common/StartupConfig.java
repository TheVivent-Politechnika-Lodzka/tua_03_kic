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

        createImplant();
        createImplantSecond();
        em.flush();

        createAdmin();
        createSpecialistAdmin();
        createClientAdmin();
        createClient();
        createSpecialist();
        em.flush();

        createAppointment();
        em.flush();

        createImplantReview();
        em.flush();
    }

    public void createImplant() {
        Implant implant = new Implant();
        implant.setName("Zestaw słuchowy");
        implant.setDescription("Implant słuchowy montowany po obu skroniach pozwala usłyszeć na odległość do 300m. Dodatkowo w zestawie naprowadzacz laserowy do precyzyjnego określenia źródła dźwięku. Zabieg nieinwazyjny wykonany w znieczuleniu miejscowym, nie jest wymagana opieka pozabiegowa.");
        implant.setManufacturer("HearMeOUT SP.Z.O.O.");
        implant.setPrice(820);
        implant.setPopularity(0);
        implant.setDuration(Duration.between(LocalTime.NOON, LocalTime.MAX));
        implant.setImage("https://cdn3.whatculture.com/images/2020/06/f9c87f9aba31102e-600x338.jpg");
        em.persist(implant);
    }

    public void createImplantSecond() {
        Implant implant = new Implant();
        implant.setName("Ręka z tytanu");
        implant.setDescription("Ręka wykonana z tytanu z 4 siłownikami klasy AB pozwalającymi wygenerować siłę 1582J czyli 3 razy wiekszą niż MAG-08. Zabieg wykonany na pełnym znieczuleniu, zabieg można wykonać zrówno mając rekę, jak również już z amputowaną ręką.");
        implant.setManufacturer("NiceHands SP.Z.O.O.");
        implant.setPrice(750);
        implant.setPopularity(0);
        implant.setDuration(Duration.between(LocalTime.NOON, LocalTime.MAX));
        implant.setImage("https://i.wpimg.pl/c/646x/m.gadzetomania.pl/joihnny-720x405-ec53e56439bdc8de.png");
        em.persist(implant);
    }

    private void createAdmin() {
        Account admin = new Account();
        admin.setLogin("administrator");
        admin.setPassword(hashAlgorithm.generate("Password123!".toCharArray()));
        admin.setActive(true);
        admin.setConfirmed(true);
        admin.setFirstName("Rache");
        admin.setLastName("Bartmoss");
        admin.setEmail("szurySSBD3@gmail.com");
        admin.setLanguage(new Locale("pl"));
        admin.setUrl("https://i0.wp.com/grynieznane.pl/wp-content/uploads/2020/12/a1-19.jpg?resize=350%2C393&ssl=1");
        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("596468753");
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
        admin.setFirstName("Yui");
        admin.setLastName("Arasaka");
        admin.setEmail("szurySSBD2@gmail.com");
        admin.setLanguage(new Locale("en"));
        admin.setUrl("https://cdnb.artstation.com/p/assets/images/images/028/593/729/large/katerina-argonskaya-cyberplague-doc-5-774.jpg?1594914723");
        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("846548753");
        dataAdministrator.setContactEmail("specadmin@kic.agency");
        DataSpecialist dataSpecialist = new DataSpecialist();
        dataSpecialist.setPhoneNumber("846548753");
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
        admin.setFirstName("Adam");
        admin.setLastName("Smasher");
        admin.setEmail("szurySSBD@gmail.com");
        admin.setLanguage(new Locale("pl"));
        admin.setUrl("https://cdnb.artstation.com/p/assets/images/images/027/342/675/large/ekaterina-ladanuk-sithh-s.jpg?1591263812");

        DataAdministrator dataAdministrator = new DataAdministrator();
        dataAdministrator.setPhoneNumber("556615151");
        dataAdministrator.setContactEmail("klientadmin@kic.agency");

        DataClient dataClient = new DataClient();
        dataClient.setPesel("51611516115");
        dataClient.setPhoneNumber("556615151");

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
        client.setFirstName("Sasai");
        client.setLastName("Arasaka");
        client.setEmail("szurySSBDclient@gmail.com");
        client.setLanguage(new Locale("pl"));
        client.setUrl("https://i.pinimg.com/originals/53/a2/86/53a286fffffddc37fda5e209723039ce.jpg");

        DataClient dataClient = new DataClient();
        dataClient.setPesel("51651615556");
        dataClient.setPhoneNumber("956456465");

        client.addAccessLevel(dataClient);
        em.persist(client);
    }

    public void createSpecialist() {
        Account specialist = new Account();
        specialist.setLogin("spec");
        specialist.setPassword(hashAlgorithm.generate("Password123!".toCharArray()));
        specialist.setActive(true);
        specialist.setConfirmed(true);
        specialist.setFirstName("Johnny");
        specialist.setLastName("Silverhand");
        specialist.setEmail("szurySSBDspec@gmail.com");
        specialist.setLanguage(new Locale("pl"));
        specialist.setUrl("https://i.pinimg.com/originals/12/da/11/12da11fad537e03c4b40af7d2e12bfce.jpg");
        DataSpecialist dataSpecialist = new DataSpecialist();
        dataSpecialist.setPhoneNumber("895111554");
        dataSpecialist.setContactEmail("specjalista@gmail.com");

        specialist.addAccessLevel(dataSpecialist);
        em.persist(specialist);
    }


//
//    public void createAppointment() {
//
//        Account accountClient = em.createNamedQuery("Account.findByLogin", Account.class)
//                .setParameter("login", "client").getSingleResult();
//        Account accountSpecialist = em.createNamedQuery("Account.findByLogin", Account.class)
//                .setParameter("login", "spec").getSingleResult();
//        List<Implant> implants = em.createNamedQuery("Implant.findAll", Implant.class).getResultList();
//
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

        Account clientAdmin = em.createNamedQuery("Account.findByLogin", Account.class).setParameter("login", "clientAdmin").getSingleResult();
        Account specialist = em.createNamedQuery("Account.findByLogin", Account.class).setParameter("login", "spec").getSingleResult();

        Implant implant = new Implant();
        implant.setName("Noktowizor");
        implant.setDescription("Pierwszą przełomową innowacją jest nasza funkcja „powiększania według własnego uznania”. Korzystając z unikalnego podwójnego FOV i 2-krotnego zoomu optycznego, użytkownik może szybko ręcznie przełączać podwójne FOV. Większe FOV o ogniskowej 25 mm jest używane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spełnij potrzebę patrzenia daleko, dokładnie i wyraźnie.");
        implant.setPrice(100);
        implant.setManufacturer("Nokto S.A.");
        implant.setPopularity(0);
        implant.setDuration(Duration.ofMinutes(30));
        implant.setImage("https://api.culture.pl/sites/default/files/styles/440_auto/public/2020-11/cyberpunk2077-outnumbered_but_not_outgunned-rgb-en.jpg?itok=ktGxus53");


        em.persist(implant);

        Appointment appointment = new Appointment();
        appointment.setClient(clientAdmin); // TUTAJ ZMIENIAĆ DO TESTÓW
        appointment.setSpecialist(specialist);
        appointment.setImplant(implant);
        appointment.setStartDate(Instant.now());
        appointment.setEndDate(Instant.now().plusSeconds(100000)); // zmieniono do testow
        appointment.setStatus(Status.ACCEPTED); // TUTAJ ZMIENIAĆ DO TESTÓW
        appointment.setPrice(100);
        appointment.setDescription("8 godzin przed zabiegiem nie można nic spożywać. Na rekonwalenstecje należy przeznaczyć 10 dni. ");

        em.persist(appointment);
    }

    public void createImplantReview() {
        ImplantReview review = new ImplantReview();
        review.setReview("Wszystko sprawnie, Pani wykonująca zabieg bardzo miła, przyjemna. Polecam");
        review.setRating(5);

        Implant implant = new Implant();

        implant.setName("Ostrze automatyczne");
        implant.setDescription("Głownia o wyraźnie zaznaczonej krzywiźnie (o wyraźnym łuku) brzucha ostrza i ostrym, lekko wklęsłym spadku grzbietu w kierunku czubka. Często wklęsły odcinek grzbietu przy czubku jest zaostrzony, lub posiada możliwość naostrzenia. Ze względu na bardzo dobre właściwości tnące przy równocześnie wysokiej zdolności do przebijania profil ten jest powszechnie wykorzystywany w nożach bojowych i roboczych ogólnego przeznaczenia.  Wadą głowni typu bowie jest stosunkowo niska wytrzymałość czubka.");
        implant.setPrice(100);
        implant.setManufacturer("Factory knifes sharp");
        implant.setPopularity(0);
        implant.setDuration(Duration.ofMinutes(120));
        implant.setImage("https://assets.reedpopcdn.com/cyberpunk-2077-ostrza-modliszkowe-jak-zdobyc-1607957173057.jpg/BROK/thumbnail/1600x900/quality/100/cyberpunk-2077-ostrza-modliszkowe-jak-zdobyc-1607957173057.jpg");

        em.persist(implant);

        Account client = em.createNamedQuery("Account.findByLogin", Account.class)
                .setParameter("login", "client")
                .getSingleResult();

        review.setClient(client);
        review.setImplant(implant);
        em.persist(review);
    }
}

package pl.lodz.p.it.ssbd2022.ssbd03.common;

import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Status;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataAdministrator;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataSpecialist;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.HashAlgorithm;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.logging.Logger;

@Startup
@Singleton
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class StartupConfig {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(StartupConfig.class.toString());

    @PersistenceContext(unitName = "ssbd03admin")
    private EntityManager em;

    @Inject
    private HashAlgorithm hashAlgorithm;

    @PostConstruct
    public void init() {
        logger.warning("##############################################################");
        logger.warning("                       STARTING UP");
        logger.warning("##############################################################");
//        try {
//            createImplant();
//        } catch (Exception e) {
//            logger.warning("implant");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
//        try {
//            createImplantSecond();
//        } catch (Exception e) {
//            logger.warning("implant second");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
//
//        try {
//            createAdmin();
//        } catch (Exception e) {
//            logger.warning("account admin");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
//        try {
//            createSpecialistAdmin();
//        } catch (Exception e) {
//            logger.warning("account specialist admin");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
//        try {
//            createClientAdmin();
//        } catch (Exception e) {
//            logger.warning("account client admin");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
//        try {
//            createSpecialist();
//        } catch (Exception e) {
//            logger.warning("account specialist");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
//        try {
//            createClient();
//        } catch (Exception e) {
//            logger.warning("account client");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
//
//        try {
//            createAppointment();
//        } catch (Exception e) {
//            logger.warning("appointment");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
//
//        try {
//            createImplantReview();
//        } catch (Exception e) {
//            logger.warning("implant review");
//            logger.info("Error during startup config: " + e.getMessage());
//            throw e;
//        }
        logger.warning("##############################################################");
        logger.warning("                        STARTED");
        logger.warning("##############################################################");
    }

    public void createImplant() {
//        em.getTransaction().begin();
        Implant implant = new Implant();
        implant.setName("Zestaw s??uchowy");
        implant.setDescription("Implant s??uchowy montowany po obu skroniach pozwala us??ysze?? na odleg??o???? do 300m. Dodatkowo w zestawie naprowadzacz laserowy do precyzyjnego okre??lenia ??r??d??a d??wi??ku. Zabieg nieinwazyjny wykonany w znieczuleniu miejscowym, nie jest wymagana opieka pozabiegowa.");
        implant.setManufacturer("HearMeOUT SP.Z.O.O.");
        implant.setPrice(820);
        implant.setPopularity(0);
        implant.setDuration(Duration.between(LocalTime.NOON, LocalTime.MAX));
        implant.setImage("https://cdn3.whatculture.com/images/2020/06/f9c87f9aba31102e-600x338.jpg");
        if (em == null) {
            logger.severe("em is null");
        }
        em.persist(implant);
        em.flush();
//        em.getTransaction().commit();
    }

    public void createImplantSecond() {
//        em.getTransaction().begin();
        Implant implant = new Implant();
        implant.setName("R??ka z tytanu");
        implant.setDescription("R??ka wykonana z tytanu z 4 si??ownikami klasy AB pozwalaj??cymi wygenerowa?? si???? 1582J czyli 3 razy wieksz?? ni?? MAG-08. Zabieg wykonany na pe??nym znieczuleniu, zabieg mo??na wykona?? zr??wno maj??c rek??, jak r??wnie?? ju?? z amputowan?? r??k??.");
        implant.setManufacturer("NiceHands SP.Z.O.O.");
        implant.setPrice(750);
        implant.setPopularity(0);
        implant.setDuration(Duration.between(LocalTime.NOON, LocalTime.MAX));
        implant.setImage("https://i.wpimg.pl/c/646x/m.gadzetomania.pl/joihnny-720x405-ec53e56439bdc8de.png");
        em.persist(implant);
        em.flush();
//        em.getTransaction().commit();
    }

    private void createAdmin() {
//        em.getTransaction().begin();
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
        em.flush();
//        em.getTransaction().commit();
    }

    private void createSpecialistAdmin() {
//        em.getTransaction().begin();
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
        em.flush();
//        em.getTransaction().commit();
    }

    private void createClientAdmin() {
//        em.getTransaction().begin();
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
        em.flush();
//        em.getTransaction().commit();
    }

    public void createClient() {
//        em.getTransaction().begin();
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
        em.flush();
//        em.getTransaction().commit();
    }

    public void createSpecialist() {
//        em.getTransaction().begin();
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
        em.flush();
//        em.getTransaction().commit();
    }

    public void createAppointment() {
//        em.getTransaction().begin();

        Account clientAdmin = em.createNamedQuery("Account.findByLogin", Account.class).setParameter("login", "clientAdmin").getSingleResult();
        Account specialist = em.createNamedQuery("Account.findByLogin", Account.class).setParameter("login", "spec").getSingleResult();

        Implant implant = new Implant();
        implant.setName("Noktowizor");
        implant.setDescription("Pierwsz?? prze??omow?? innowacj?? jest nasza funkcja ???powi??kszania wed??ug w??asnego uznania???. Korzystaj??c z unikalnego podw??jnego FOV i 2-krotnego zoomu optycznego, u??ytkownik mo??e szybko r??cznie prze????cza?? podw??jne FOV. Wi??ksze FOV o ogniskowej 25 mm jest u??ywane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spe??nij potrzeb?? patrzenia daleko, dok??adnie i wyra??nie.");
        implant.setPrice(100);
        implant.setManufacturer("Nokto S.A.");
        implant.setPopularity(0);
        implant.setDuration(Duration.ofMinutes(30));
        implant.setImage("https://api.culture.pl/sites/default/files/styles/440_auto/public/2020-11/cyberpunk2077-outnumbered_but_not_outgunned-rgb-en.jpg?itok=ktGxus53");

        em.persist(implant);

        Implant implant2 = new Implant();
        implant2.setName("W??osy-kable");
        implant2.setDescription("Pierwsz?? prze??omow?? innowacj?? jest nasza funkcja ???powi??kszania wed??ug w??asnego uznania???. Korzystaj??c z unikalnego podw??jnego FOV i 2-krotnego zoomu optycznego, u??ytkownik mo??e szybko r??cznie prze????cza?? podw??jne FOV. Wi??ksze FOV o ogniskowej 25 mm jest u??ywane do wyszukiwania celu, a mniejsze FOV o ogniskowej 50 mm do identyfikacji celu. Spe??nij potrzeb?? patrzenia daleko, dok??adnie i wyra??nie.");
        implant2.setPrice(250);
        implant2.setManufacturer("Nokto S.A.");
        implant2.setPopularity(0);
        implant2.setDuration(Duration.ofMinutes(30));
        implant2.setImage("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSHTlU2gK88_xJfgB5Wm0Rk0qYY4bf_-Y-gfw&usqp=CAU");

        em.persist(implant2);

        for (int i = 0; i < 3; i++) {
            Instant now = Instant.now().minus(i + 1, ChronoUnit.DAYS);
            Appointment appointment = new Appointment();
            appointment.setClient(clientAdmin); // TUTAJ ZMIENIA?? DO TEST??W
            appointment.setSpecialist(specialist);
            appointment.setImplant(implant);
            appointment.setStartDate(now);
            appointment.setEndDate(now.plus(2, ChronoUnit.HOURS)); // zmieniono do testow
            appointment.setStatus(Status.ACCEPTED); // TUTAJ ZMIENIA?? DO TEST??W
            appointment.setPrice(implant.getPrice());
            appointment.setDescription("8 godzin przed zabiegiem nie mo??na nic spo??ywa??. Na rekonwalenscencje nale??y przeznaczy?? 10 dni. ");

            em.persist(appointment);
        }

        for (int i = 0; i < 3; i++) {
            Instant now = Instant.now().plus(i + 1, ChronoUnit.DAYS);
            Appointment appointment = new Appointment();
            appointment.setClient(clientAdmin); // TUTAJ ZMIENIA?? DO TEST??W
            appointment.setSpecialist(specialist);
            appointment.setImplant(implant2);
            appointment.setStartDate(now);
            appointment.setEndDate(now.plus(2, ChronoUnit.HOURS)); // zmieniono do testow
            appointment.setStatus(Status.PENDING); // TUTAJ ZMIENIA?? DO TEST??W
            appointment.setPrice(implant2.getPrice());
            appointment.setDescription("8 godzin przed zabiegiem nie mo??na nic spo??ywa??. Na rekonwalenscencje nale??y przeznaczy?? 10 dni. ");

            em.persist(appointment);
        }
        em.flush();
//        em.getTransaction().commit();
    }

    public void createImplantReview() {
//        em.getTransaction().begin();
        ImplantReview review = new ImplantReview();
        review.setReview("Wszystko sprawnie, Pani wykonuj??ca zabieg bardzo mi??a, przyjemna. Polecam");
        review.setRating(5);

        Implant implant = new Implant();

        implant.setName("Ostrze automatyczne");
        implant.setDescription("G??ownia o wyra??nie zaznaczonej krzywi??nie (o wyra??nym ??uku) brzucha ostrza i ostrym, lekko wkl??s??ym spadku grzbietu w kierunku czubka. Cz??sto wkl??s??y odcinek grzbietu przy czubku jest zaostrzony, lub posiada mo??liwo???? naostrzenia. Ze wzgl??du na bardzo dobre w??a??ciwo??ci tn??ce przy r??wnocze??nie wysokiej zdolno??ci do przebijania profil ten jest powszechnie wykorzystywany w no??ach bojowych i roboczych og??lnego przeznaczenia.  Wad?? g??owni typu bowie jest stosunkowo niska wytrzyma??o???? czubka.");
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
        em.flush();
//        em.getTransaction().commit();
    }
}

package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.util.List;
import java.util.UUID;

public class AppointmentFacade extends AbstractFacade<Appointment> {

    @PersistenceContext(unitName = "ssbd03mopPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public AppointmentFacade() {
        super(Appointment.class);
    }

    /**
     * Metoda zwracająca wszystkie wizyty użytkownika o podanym loginie
     * @param login Login użytkownika
     * @return Lista wizyt użytkownika o podanym loginie
     */
    public List<Appointment> findByClientLogin(String login) {
        TypedQuery<Appointment> typedQuery = entityManager.createNamedQuery("Appointment.findByClientLogin", Appointment.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getResultList();
    }
}

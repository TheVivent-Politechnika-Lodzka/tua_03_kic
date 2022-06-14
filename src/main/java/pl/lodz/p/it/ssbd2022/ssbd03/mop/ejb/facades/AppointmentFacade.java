package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

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
     *
     * @param login Login użytkownika
     * @return Lista wizyt użytkownika o podanym loginie
     */
    public List<Appointment> findByClientLogin(String login) {
        TypedQuery<Appointment> typedQuery = entityManager.createNamedQuery("Appointment.findByClientLogin", Appointment.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getResultList();
    }

    /**
     * Metoda zwracająca listę wizyt
     *
     * @param pageNumber numer aktualnie przeglądanej strony
     * @param perPage ilość rekordów na danej stronie
     * @param phrase wyszukiwana fraza
     * @return  Lista wizyt zgodnych z parametrami wyszukiwania
     * @throws InvalidParametersException w przypadku podania nieprawidłowych parametrów
     * @throws DatabaseException w przypadku wystąpienia błędu bazy danych
     */
    @PermitAll
    public PaginationData findInRangeWithPhrase(int pageNumber, int perPage, String phrase) {
        try {
            TypedQuery<Appointment> typedQuery = entityManager.createNamedQuery("Appointment.searchByPhrase", Appointment.class);

            pageNumber--;

            List<Appointment> data = typedQuery.setParameter("phrase", "%" + phrase + "%")
                    .setMaxResults(perPage)
                    .setFirstResult(pageNumber * perPage)
                    .getResultList();

            pageNumber++;
            int totalCount = this.count();
            int totalPages = (int) Math.ceil((double) totalCount / perPage);

            return new PaginationData(totalCount, totalPages, pageNumber, data);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e.getCause());
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }
}

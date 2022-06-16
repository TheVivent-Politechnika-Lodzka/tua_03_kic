package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Appointment;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;
import java.util.UUID;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
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
     * Metoda edytująca wizytę w bazie danych. Uwzględnia wersję
     *
     * @param entity
     */
    @Override
    @RolesAllowed(Roles.AUTHENTICATED)
    public void edit(Appointment entity) {
        super.edit(entity);
    }


    /**
     * Metoda wyszukująca konkretną wizytę względem wprowadzonego identyfikatora
     *
     * @param id Identyfikator poszukiwanej wizyty
     * @return Obiekt znalezionej wizyty
     * @throws InvalidParametersException, gdy podano niepoprawną wartość parametru
     * @throws DatabaseException,          gdy wystąpi błąd związany z bazą danych
     */
    @PermitAll
    public Appointment findById(UUID id) {
        try {
            TypedQuery<Appointment> typedQuery = entityManager.createNamedQuery("Appointment.findById", Appointment.class);
            typedQuery.setParameter("id", id);
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
        } catch (IllegalArgumentException iae) {
            throw new InvalidParametersException(iae.getCause());
        } catch (PersistenceException pe) {
            throw new DatabaseException(pe.getCause());
        }
    }

    /**
     * Metoda zwracająca listę wizyt
     *
     * @param pageNumber numer aktualnie przeglądanej strony
     * @param perPage    ilość rekordów na danej stronie
     * @param phrase     wyszukiwana fraza
     * @return Lista wizyt zgodnych z parametrami wyszukiwania
     * @throws InvalidParametersException w przypadku podania nieprawidłowych parametrów
     * @throws DatabaseException          w przypadku wystąpienia błędu bazy danych
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
    /**
     * Metoda zwracająca wybraną ilość wizyty użytkownika o podanym loginie
     *
     * @param login Login użytkownika
     * @param pageNumber numer aktualnie przeglądanej strony
     * @param perPage    ilość rekordów na danej stronie
     * @return Lista wizyt użytkownika o podanym loginie
     */
    public PaginationData findByClientLoginInRange(String login,int pageNumber, int perPage) {
        try {
            TypedQuery<Appointment> typedQuery = entityManager.createNamedQuery("Appointment.findByClientLogin", Appointment.class);

            pageNumber--;

            List<Appointment> data = typedQuery.setParameter("login",login)
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

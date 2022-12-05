package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

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

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class AppointmentMOPFacade extends AbstractFacade<Appointment> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03mop")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public AppointmentMOPFacade() {
        super(Appointment.class);
    }

    /**
     * Metoda zwracająca wszystkie wizyty użytkownika o podanym loginie
     *
     * @param login Login użytkownika
     * @return Lista wizyt użytkownika o podanym loginie
     */
    @RolesAllowed(Roles.CLIENT)
    public List<Appointment> findByClientLogin(String login) {
        TypedQuery<Appointment> typedQuery = entityManager.createNamedQuery("Appointment.findByClientLogin", Appointment.class);
        typedQuery.setParameter("login", login);
        return typedQuery.getResultList();
    }

    /**
     * Metoda tworząca wizytę
     *
     * @param entity - wizyta
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public void create(Appointment entity) {
        super.create(entity);
    }

    /**
     * zwraca listę wizyt dla danego specjalisty w danym okresie
     *
     * @param specialistId - id specjalisty
     * @param startDate    - data startowa
     * @param endDate      - data końcowa
     * @param pageNumber   - numer strony
     * @param perPage      - ilość wyników na stronę
     * @return wynik
     */
    @RolesAllowed({Roles.SPECIALIST, Roles.CLIENT})
    public PaginationData findSpecialistAppointmentsInGivenPeriod(UUID specialistId, Instant startDate, Instant endDate, int pageNumber, int perPage) {
        List<Appointment> data = entityManager
                .createNamedQuery("Appointment.findSpecialistAppointmentsInGivenPeriod", Appointment.class)
                .setParameter("specialistId", specialistId.toString())
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .setFirstResult((pageNumber - 1) * perPage)
                .setMaxResults(perPage)
                .getResultList();

        Long totalCount = entityManager
                .createNamedQuery("Appointment.findSpecialistAppointmentsInGivenPeriod.count", Long.class)
                .setParameter("specialistId", specialistId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();

        int totalPages = (int) Math.ceil(totalCount / (double) perPage);
        PaginationData paginationData = new PaginationData(
                totalCount.intValue(),
                totalPages,
                perPage,
                data
        );
        return paginationData;
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
     * @throws ResourceNotFoundException,  gdy nie znaleziono wizyty
     * @throws DatabaseException,          gdy wystąpi błąd związany z bazą danych
     */
    @RolesAllowed(Roles.AUTHENTICATED)
    public Appointment findById(UUID id) {
        try {
            TypedQuery<Appointment> typedQuery = entityManager.createNamedQuery("Appointment.findById", Appointment.class);
            typedQuery.setParameter("id", id.toString());
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
     * @throws InvalidParametersException, w przypadku podania nieprawidłowych parametrów
     * @throws DatabaseException,          w przypadku wystąpienia błędu bazy danych
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    public PaginationData findInRangeWithPhrase(int pageNumber, int perPage, String phrase) {
        try {
            List<Appointment> data = entityManager
                    .createNamedQuery("Appointment.searchByPhrase", Appointment.class)
                    .setParameter("phrase", "%" + phrase + "%")
                    .setMaxResults(perPage)
                    .setFirstResult((pageNumber - 1) * perPage)
                    .getResultList();

            Long totalCount = entityManager
                    .createNamedQuery("Appointment.searchByPhrase.count", Long.class)
                    .setParameter("phrase", "%" + phrase + "%")
                    .getSingleResult();

            int totalPages = (int) Math.ceil((double) totalCount / perPage);

            return new PaginationData(totalCount.intValue(), totalPages, pageNumber, data);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e);
        } catch (PersistenceException e) {
            e.printStackTrace();
            throw new DatabaseException(e);
        }
    }

    /**
     * Metoda zwracająca wybraną ilość wizyty użytkownika o podanym loginie
     *
     * @param login      Login użytkownika
     * @param pageNumber numer aktualnie przeglądanej strony
     * @param perPage    ilość rekordów na danej stronie
     * @return Lista wizyt użytkownika o podanym loginie
     * @throws InvalidParametersException w przypadku podania nieprawidłowych parametrów
     * @throws DatabaseException          w przypadku wystąpienia błędu bazy danych
     */
    @RolesAllowed({Roles.CLIENT, Roles.SPECIALIST})
    public PaginationData findByClientLoginInRange(int pageNumber, int perPage, String login) {
        try {
            List<Appointment> data = entityManager
                    .createNamedQuery("Appointment.findByLogin", Appointment.class)
                    .setParameter("login", login)
                    .setMaxResults(perPage)
                    .setFirstResult((pageNumber - 1) * perPage)
                    .getResultList();

            int totalCount = entityManager
                    .createNamedQuery("Appointment.findByLogin", Appointment.class)
                    .setParameter("login", login)
                    .getResultList()
                    .size();

            int totalPages = (int) Math.ceil((double) totalCount / perPage);

            return new PaginationData(totalCount, totalPages, pageNumber, data);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e.getCause());
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }
}

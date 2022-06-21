package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.exception.ConstraintViolationException;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.common.Roles;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant.ImplantAlreadyExistExceptions;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.util.UUID;

import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ImplantFacade extends AbstractFacade<Implant> {

    @PersistenceContext(unitName = "ssbd03mopPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public ImplantFacade() {
        super(Implant.class);
    }

    /**
     * Metoda dodająca implant do bazy danych
     *
     * @param entity - implant
     * @throws ImplantAlreadyExistExceptions - wyjątek rzucany w przypadku, gdy implant o podanej nazwie już istnieje w bazie danych
     */
    @RolesAllowed(Roles.ADMINISTRATOR)
    @Override
    public void create(Implant entity) {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getConstraintName().contains(Implant.CONSTRAINT_NAME_UNIQUE)) {
                throw ImplantAlreadyExistExceptions.nameExists();
            }
            throw new DatabaseException(e);
        }
    }

    /**
     * Metoda edytująca implant w bazie danych.
     *
     * @param entity
     * @throws ImplantAlreadyExistExceptions - wyjątek rzucany w przypadku, gdy implant o podanej nazwie już istnieje w bazie danych
     * @throws DatabaseException - gdy wystąpi błąd związany z bazą danych
     */
    @Override
    @RolesAllowed({Roles.ADMINISTRATOR, Roles.SPECIALIST})
    public void edit(Implant entity) {
        try {
            super.edit(entity);
        } catch (ConstraintViolationException e) {
            if (e.getConstraintName().contains(Implant.CONSTRAINT_NAME_UNIQUE)) {
                throw ImplantAlreadyExistExceptions.nameExists();
            }
            throw new DatabaseException(e);
        }
    }


    /**
     * Metoda wyszukująca konkretny implant w bazie danych
     *
     * @param uuid - uuid implantu
     * @return implant
     * @throws InvalidParametersException, gdy podano niepoprawną wartość parametru
     * @throws DatabaseException, gdy wystąpi błąd związany z bazą danych
     */
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public Implant findByUUID(UUID uuid) {
        try {
            TypedQuery<Implant> typedQuery = entityManager.createNamedQuery("Implant.findById", Implant.class);
            typedQuery.setParameter("id", uuid);
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
     * Metoda do zwracania listy wszczepów
     *
     * @param pageNumber - numer strony
     * @param perPage    - ilość pozycji na stronie
     * @param phrase     - szukana fraza
     * @param archived   określa czy zwracac archiwalne czy niearchiwalne wszczepy
     * @return lista wszczepów
     * @throws InvalidParametersException jeśli podano nieprawidłowe parametry
     * @throws DatabaseException jeśli wystąpił błąd z bazą danych
     */
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public PaginationData findInRangeWithPhrase(int pageNumber, int perPage, String phrase, boolean archived) {
        try {
            TypedQuery<Implant> typedQuery = entityManager.createNamedQuery("Implant.searchByPhrase", Implant.class);

            pageNumber--;

            List<Implant> data = typedQuery.setParameter("phrase", "%" + phrase + "%")
                    .setParameter("archived", archived)
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

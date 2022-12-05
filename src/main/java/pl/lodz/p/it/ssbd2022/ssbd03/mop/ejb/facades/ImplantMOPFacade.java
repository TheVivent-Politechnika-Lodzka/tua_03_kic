package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import lombok.Getter;
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
public class ImplantMOPFacade extends AbstractFacade<Implant> {

    private static final long serialVersionUID = 1L;

    @PersistenceContext(unitName = "ssbd03mop")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    public ImplantMOPFacade() {
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
            if (e.getConstraintViolations().contains(Implant.CONSTRAINT_NAME_UNIQUE)) {
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
            if (e.getConstraintViolations().contains(Implant.CONSTRAINT_NAME_UNIQUE)) {
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
            typedQuery.setParameter("id", uuid.toString());
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
            List<Implant> data = entityManager
                    .createNamedQuery("Implant.searchByPhrase", Implant.class)
                    .setParameter("phrase", "%" + phrase + "%")
                    .setParameter("archived", archived)
                    .setMaxResults(perPage)
                    .setFirstResult((pageNumber - 1) * perPage)
                    .getResultList();

            Long totalCount = entityManager
                    .createNamedQuery("Implant.searchByPhrase.count", Long.class)
                    .setParameter("phrase", "%" + phrase + "%")
                    .setParameter("archived", archived)
                    .getSingleResult();

            int totalPages = (int) Math.ceil((double) totalCount / perPage);

            return new PaginationData(totalCount.intValue(), totalPages, pageNumber, data);
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e.getCause());
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }

}

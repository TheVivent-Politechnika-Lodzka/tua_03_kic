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
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review.ImplantReviewAlreadyExistsException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
import pl.lodz.p.it.ssbd2022.ssbd03.utils.PaginationData;

import java.util.List;
import java.util.UUID;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ImplantReviewMOPFacade extends AbstractFacade<ImplantReview> {

    private static final long serialVersionUID = 1L;

    public ImplantReviewMOPFacade() {
        super(ImplantReview.class);
    }

    @PersistenceContext(unitName = "ssbd03mop")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    /**
     * Metoda zwracająca receznję implantu o podanym identyfikatorze
     *
     * @param uuid Identyfikator recenzji implantu
     * @return Recenzja implantu o podanym identyfikatorze
     * @throws ResourceNotFoundException  - wyjątek rzucany w przypadku, gdy nie znaleziono recenzji implantu o podanym identyfikatorze
     * @throws InvalidParametersException - wyjątek rzucany w przypadku, gdy podano nieprawidłowy identyfikator recenzji implantu
     * @throws DatabaseException          - wyjątek rzucany w przypadku błędu związanego z bazą danych
     */
    @RolesAllowed({Roles.CLIENT, Roles.ADMINISTRATOR})
    public ImplantReview findByUUID(UUID uuid) {
        try {
            TypedQuery<ImplantReview> typedQuery = entityManager.createNamedQuery("Review.findById", ImplantReview.class);
            typedQuery.setParameter("id", uuid.toString());
            return typedQuery.getSingleResult();
        } catch (NoResultException e) {
            throw new ResourceNotFoundException();
        } catch (IllegalArgumentException e) {
            throw new InvalidParametersException(e.getCause());
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }

    /**
     * Metoda dodająca recenzję implantu do bazy danych
     *
     * @param entity Recenzja implantu
     * @throws ImplantReviewAlreadyExistsException - wyjątek rzucany w przypadku, gdy recenzja dla danego implantu,
     *                                             napisana przez tego samego użytkownika istnieje
     *                                             w bazie danych
     *                                             (nie można dodawać dwóch recenzji dla tego samego implantu przez tego samego użytkownika)
     * @throws DatabaseException                   - wyjątek rzucany w przypadku błędu związanego z bazą danych
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public void create(ImplantReview entity) {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getConstraintViolations().contains(ImplantReview.CONSTRAINT_ONE_REVIEW_PER_CLIENT_PER_IMPLANT)) {
                throw new ImplantReviewAlreadyExistsException();
            }
            throw new DatabaseException(e);
        }
    }

    /**
     * Metoda usuwająca recenzję implantu z bazy danych
     *
     * @param entity Recenzja implantu
     * @throws DatabaseException - wyjątek rzucany w przypadku błędu związanego z bazą danych
     */
    @Override
    @RolesAllowed({Roles.ADMINISTRATOR, Roles.CLIENT})
    public void remove(ImplantReview entity) {
        try {
            super.unsafeRemove(entity);
        } catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }


    /**
     * Metoda zwracająca recenzje dla danego implantu
     *
     * @param id Identyfikator implantu
     * @param pageNumber numer aktualnie przeglądanej strony
     * @param perPage ilość rekordów na danej stronie
     * @return Lista wizyt użytkownika o podanym loginie
     * @throws InvalidParametersException w przypadku podania nieprawidłowych parametrów
     * @throws DatabaseException w przypadku wystąpienia błędu bazy danych
     */
    @RolesAllowed({Roles.ANONYMOUS, Roles.AUTHENTICATED})
    public PaginationData findInRangeWithPhrase(int pageNumber, int perPage, UUID id) {
        try {
            List<ImplantReview> data = entityManager
                    .createNamedQuery("Review.findByImplantId", ImplantReview.class)
                    .setParameter("implantId", id.toString())
                    .setMaxResults(perPage)
                    .setFirstResult((pageNumber-1) * perPage)
                    .getResultList();

            Long totalCount = entityManager
                    .createNamedQuery("Review.findByImplantId.count", Long.class)
                    .setParameter("implantId", id.toString())
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

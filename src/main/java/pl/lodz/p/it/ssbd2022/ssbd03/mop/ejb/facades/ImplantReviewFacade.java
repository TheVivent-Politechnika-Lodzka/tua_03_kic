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
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.ResourceNotFoundException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.implant_review.ImplantReviewAlreadyExistsException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;

import java.util.UUID;

@Interceptors(TrackerInterceptor.class)
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ImplantReviewFacade extends AbstractFacade<ImplantReview> {

    public ImplantReviewFacade() {
        super(ImplantReview.class);
    }

    @PersistenceContext(unitName = "ssbd03mopPU")
    @Getter
    private EntityManager entityManager;

    @Inject
    @Getter
    private Tagger tagger;

    /**
     * Metoda zwracająca receznję implantu o podanym identyfikatorze
     * @param uuid Identyfikator recenzji implantu
     * @return Recenzja implantu o podanym identyfikatorze
     * @throws ResourceNotFoundException - wyjątek rzucany w przypadku, gdy nie znaleziono recenzji implantu o podanym identyfikatorze
     * @throws InvalidParametersException - wyjątek rzucany w przypadku, gdy podano nieprawidłowy identyfikator recenzji implantu
     * @throws DatabaseException - wyjątek rzucany w przypadku błędu związanego z bazą danych
     */
    public ImplantReview findByUUID(UUID uuid) {
        try {
            TypedQuery<ImplantReview> typedQuery = entityManager.createNamedQuery("Review.findById", ImplantReview.class);
            typedQuery.setParameter("id", uuid);
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
     *                                            (nie można dodawać dwóch recenzji dla tego samego implantu przez tego samego użytkownika)
     * @throws DatabaseException - wyjątek rzucany w przypadku błędu związanego z bazą danych
     */
    @Override
    @RolesAllowed(Roles.CLIENT)
    public void create(ImplantReview entity) {
        try {
            super.create(entity);
        } catch (ConstraintViolationException e) {
            if (e.getConstraintName().equals(ImplantReview.CONSTRAINT_ONE_REVIEW_PER_CLIENT_PER_IMPLANT)) {
                throw new ImplantReviewAlreadyExistsException();
            }
            throw new DatabaseException(e);
        }
    }

    /**
     * Metoda usuwająca recenzję implantu z bazy danych
     * @param entity Recenzja implantu
     * @throws InvalidParametersException - wyjątek rzucany w przypadku, gdy podano nieprawidłowy identyfikator recenzji implantu
     * @throws DatabaseException - wyjątek rzucany w przypadku błędu związanego z bazą danych
     */
    @Override
    @RolesAllowed({Roles.ADMINISTRATOR, Roles.CLIENT})
    public void remove(ImplantReview entity) {
        try {
            super.unsafeRemove(entity);
        }  catch (PersistenceException e) {
            throw new DatabaseException(e.getCause());
        }
    }

}

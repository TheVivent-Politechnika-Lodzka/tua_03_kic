package pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades;

import jakarta.annotation.security.PermitAll;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import jakarta.persistence.*;
import lombok.Getter;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.InvalidParametersException;
import pl.lodz.p.it.ssbd2022.ssbd03.exceptions.database.DatabaseException;
import pl.lodz.p.it.ssbd2022.ssbd03.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Tagger;
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
     * Metoda do zwracania listy wszczepów
     * @param pageNumber - numer strony
     * @param perPage - ilość pozycji na stronie
     * @param phrase - szukana fraza
     * @return lista wszczepów
     * @throws InvalidParametersException jeśli podano nieprawidłowe parametry
     * @throws DatabaseException jeśli wystąpił błąd z bazą danych
     */
    @PermitAll
    public PaginationData findInRangeWithPhrase(int pageNumber, int perPage, String phrase) {
        try {
            TypedQuery<Implant> typedQuery = entityManager.createNamedQuery("Implant.searchByPhrase", Implant.class);

            pageNumber--;

            List<Implant> data = typedQuery.setParameter("phrase", "%" + phrase + "%")
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

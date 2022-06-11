package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.DataClientFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;

@Stateless
public class ImplantReviewMapper {

    @Inject
    private ImplantFacade implantFacade;

    @Inject
    private DataClientFacade dataClientFacade;

    @Inject
    private ImplantMapper implantMapper;

    /**
     * Metoda mapuje obiekt typu {@link CreateImplantReviewDto} na obiekt typu {@link ImplantReview}.
     * @param dto Obiekt typu {@link CreateImplantReviewDto}
     * @return Obiekt typu {@link ImplantReview}
     */
    public ImplantReview createImplantReviewFromDto(CreateImplantReviewDto dto) {
        ImplantReview review = new ImplantReview();

        DataClient dataClient = dataClientFacade.findByLogin(dto.getLogin());
        Implant implant = implantFacade.findByUUID(dto.getImplantId());

        review.setImplant(implant);
        review.setClient(dataClient);
        review.setReview(dto.getReview());
        review.setRating(dto.getRating());
        return review;
    }


    /**
     * Metoda mapuje obiekt typu {@link ImplantReview} na obiekt typu {@link ImplantReviewDto}.
     * @param review Obiekt typu {@link ImplantReview}
     * @return Obiekt typu {@link ImplantReviewDto}
     */
    public ImplantReviewDto implantReviewDtofromImplantReview(ImplantReview review) {
        ImplantReviewDto implantReviewDto = new ImplantReviewDto();

        String login = dataClientFacade.getLoginFromId(review.getClient().getId());

        implantReviewDto.setImplantId(review.getImplant().getId());
        implantReviewDto.setLogin(login);
        implantReviewDto.setReview(review.getReview());
        implantReviewDto.setCreatedAt(review.getCreatedAt());
        implantReviewDto.setRating(review.getRating());

        return implantReviewDto;
    }
}

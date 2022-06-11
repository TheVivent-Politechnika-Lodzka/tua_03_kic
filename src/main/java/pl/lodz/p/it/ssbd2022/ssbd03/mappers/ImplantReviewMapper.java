package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.access_levels.DataClient;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.DataClientFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;

@Stateless
public class ImplantReviewMapper {

    @Inject
    private ImplantFacade implantFacade;

    @Inject
    private DataClientFacade dataClientFacade;

    public ImplantReview createImplantReviewFromDto(CreateReviewDto dto) {
        ImplantReview review = new ImplantReview();

        DataClient dataClient = dataClientFacade.findByLogin(dto.getLogin());
        Implant implant = implantFacade.findByUUID(dto.getImplantId());

        review.setImplant(implant);
        review.setClient(dataClient);
        review.setReview(dto.getReview());
        review.setRating(dto.getRating());
        return review;
    }
}

package pl.lodz.p.it.ssbd2022.ssbd03.mappers;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Account;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.Implant;
import pl.lodz.p.it.ssbd2022.ssbd03.entities.ImplantReview;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.CreateImplantReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.dto.ImplantReviewDto;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.AccountFacade;
import pl.lodz.p.it.ssbd2022.ssbd03.mop.ejb.facades.ImplantFacade;

import java.util.ArrayList;
import java.util.List;

@Stateless
public class ImplantReviewMapper {

    @Inject
    private ImplantFacade implantFacade;

    @Inject
    private AccountFacade accountFacade;


    /**
     * Metoda mapuje obiekt typu CreateImplantReviewDto na obiekt typu ImplantReview
     *
     * @param dto Obiekt typu CreateImplantReviewDto
     * @return Obiekt typu ImplantReview
     */
    public ImplantReview createImplantReviewFromDto(CreateImplantReviewDto dto) {
        ImplantReview review = new ImplantReview();

        Account client = accountFacade.findByLogin(dto.getClientLogin());
        Implant implant = implantFacade.findByUUID(dto.getImplantId());

        review.setImplant(implant);
        review.setClient(client);
        review.setReview(dto.getReview());
        review.setRating(dto.getRating());
        return review;
    }


    /**
     * Metoda mapuje obiekt typu ImplantReview na obiekt typu ImplantReviewDto.
     *
     * @param review Obiekt typu ImplantReview
     * @return Obiekt typu ImplantReviewDto
     */
    public ImplantReviewDto implantReviewDtofromImplantReview(ImplantReview review) {
        ImplantReviewDto implantReviewDto = new ImplantReviewDto();

        implantReviewDto.setId(review.getId());
        implantReviewDto.setImplantId(review.getImplant().getId());
        implantReviewDto.setClientLogin(review.getClient().getLogin());
        implantReviewDto.setReview(review.getReview());
        implantReviewDto.setCreatedAt(review.getCreatedAt());
        implantReviewDto.setRating(review.getRating());

        return implantReviewDto;
    }

    /**
     * Metoda mapuje listę obiektów typu ImplantReview na listę obiektów typu ImplantReviewDto.
     * @param reviews Lista obiektów typu ImplantReview
     * @return Lista obiektów typu ImplantReviewDto
     */
    public List<ImplantReviewDto> implantReviewDtoListfromImplantReviewList(List<ImplantReview> reviews) {
        List<ImplantReviewDto> implantReviewDtos = new ArrayList<>();
        for (ImplantReview review : reviews) {
            implantReviewDtos.add(implantReviewDtofromImplantReview(review));
        }
        return implantReviewDtos;
    }
}

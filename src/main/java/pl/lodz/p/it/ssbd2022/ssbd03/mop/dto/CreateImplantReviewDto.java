package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Rating;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Review;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateImplantReviewDto {

    private static final long serialVersionUID = 1L;

    @NotNull
    private UUID implantId;

    @Login
    private String clientLogin;

    @Review
    private String review;

    @Rating
    private double rating;
}

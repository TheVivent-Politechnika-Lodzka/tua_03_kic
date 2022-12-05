package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Rating;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Review;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImplantReviewDto {

    private static final long serialVersionUID = 1L;

    @NotNull
    private UUID id;

    @NotNull
    private UUID implantId;

    @Login
    private String clientLogin;

    @Review
    private String review;

    @NotNull
    private Instant createdAt;

    @Rating
    private double rating;

    private String clientImage;

}

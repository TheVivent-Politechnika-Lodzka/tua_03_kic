package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Rating;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Review;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewDto {

    @NotNull
    private UUID implantId;

    @Login
    private String login;

    @Review
    private String review;

    @Rating
    private double rating;
}

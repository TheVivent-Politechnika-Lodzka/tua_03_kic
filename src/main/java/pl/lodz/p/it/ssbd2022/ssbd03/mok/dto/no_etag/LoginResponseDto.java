package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponseDto {

    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;
}

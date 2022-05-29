package pl.lodz.p.it.ssbd2022.ssbd03.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JWTStruct {
    private String accessToken;
    private String refreshToken;
}

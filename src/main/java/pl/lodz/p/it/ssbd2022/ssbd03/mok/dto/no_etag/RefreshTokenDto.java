package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenDto {

    private static final long serialVersionUID = 1L;

    private String refreshToken;
}

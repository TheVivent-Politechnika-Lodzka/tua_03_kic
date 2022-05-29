package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;


import jakarta.ejb.Stateless;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenDto {

    private String refreshToken;
}

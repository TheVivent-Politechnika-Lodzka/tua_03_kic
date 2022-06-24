package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterClientConfirmDto {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "server.error.validation.constraints.notNull.token")
    private String token;

}

package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2022.ssbd03.common.AbstractDto;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AccountDto extends AbstractDto {

    @NotNull
    private String login;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
}

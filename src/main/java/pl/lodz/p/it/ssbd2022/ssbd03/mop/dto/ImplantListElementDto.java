package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImplantListElementDto {
    @Name
    private String name;

    @Description
    private String description;

    @Price
    private int price;

    @Url
    private String url;

}

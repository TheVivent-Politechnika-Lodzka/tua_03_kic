package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Description;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Name;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Price;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Url;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImplantListElementDto {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @Name
    private String name;

    @Description
    private String description;

    @Price
    private int price;

    @Url
    private String url;

}

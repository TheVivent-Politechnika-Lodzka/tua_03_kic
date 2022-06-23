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
public class CreateImplantDto {

    private static final long serialVersionUID = 1L;

    @Name
    private String name;

    @Description
    private String description;

    @Manufacturer
    private String manufacturer;

    @Price
    private int price;

    @DurationValue
    private long duration;

    @Url
    private String url;

}

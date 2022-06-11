package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.*;

import java.time.Duration;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImplantDto implements Taggable {

    private UUID id;
    private Long version;

    @Name
    private String name;

    @Description
    private String description;

    @Manufacturer
    private String manufacturer;

    @Price
    private int price;

    private boolean archived = false;

    private int popularity;

    private Duration duration;

    @Url
    private String image;
}

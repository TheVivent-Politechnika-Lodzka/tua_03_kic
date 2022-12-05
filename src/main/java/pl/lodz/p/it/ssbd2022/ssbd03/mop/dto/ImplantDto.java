package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Description;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.DurationValue;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Manufacturer;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Name;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Price;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Url;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImplantDto implements Taggable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "server.error.validation.constraints.notNull.id")
    private UUID id;

    @NotNull(message = "server.error.validation.constraints.notNull.version")
    private Long version;

    @Name
    private String name;

    @Description
    private String description;

    @Manufacturer
    private String manufacturer;

    @Price
    private int price;

    private boolean archived;

    private int popularity;

    @DurationValue
    private long duration;

    @Url
    private String image;
}


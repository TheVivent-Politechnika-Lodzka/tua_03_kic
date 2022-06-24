package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.*;

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


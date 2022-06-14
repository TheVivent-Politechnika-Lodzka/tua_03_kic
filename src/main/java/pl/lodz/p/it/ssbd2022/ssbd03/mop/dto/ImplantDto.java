package pl.lodz.p.it.ssbd2022.ssbd03.mop.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.security.Taggable;

import java.time.Duration;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImplantDto implements Taggable {

    private UUID id;
    private Long version;

    private String name;
    private String description;
    private String manufacturer;
    private int price;
    private boolean archived;
    private int popularity;
    private Duration duration;
}

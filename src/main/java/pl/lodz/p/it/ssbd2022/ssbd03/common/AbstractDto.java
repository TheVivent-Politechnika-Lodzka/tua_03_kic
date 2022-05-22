package pl.lodz.p.it.ssbd2022.ssbd03.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractDto {

    String eTag;

    Instant createdAt;

    Instant lastModified;

}

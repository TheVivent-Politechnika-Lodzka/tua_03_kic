package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag;

import javax.json.bind.annotation.JsonbTypeDeserializer;
import javax.json.bind.annotation.JsonbTypeSerializer;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.http.nio.entity.NStringEntity;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.LocaleSerializerDeserializer;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.*;

import java.util.Locale;

@Getter
@Setter
@AllArgsConstructor
public class CreateAccountDto {

    private static final long serialVersionUID = 1L;

    @Password
    private String password;

    @Login
    private String login;

    @NotNull
    @Email
    private String email;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    @Url
    private String url;

    @NotNull(message = "server.error.validation.constraints.notNull.language")
    @JsonbTypeDeserializer(LocaleSerializerDeserializer.class)
    @JsonbTypeSerializer(LocaleSerializerDeserializer.class)
    private Locale language;

}
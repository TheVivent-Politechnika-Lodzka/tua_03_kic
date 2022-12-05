package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.LocaleSerializerDeserializer;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.FirstName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.LastName;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Login;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Password;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.Url;

import javax.json.bind.annotation.JsonbTypeDeserializer;
import javax.json.bind.annotation.JsonbTypeSerializer;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
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
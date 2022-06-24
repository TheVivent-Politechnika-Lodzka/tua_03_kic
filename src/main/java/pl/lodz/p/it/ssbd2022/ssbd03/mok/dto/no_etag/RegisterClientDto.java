package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto.no_etag;

import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.lodz.p.it.ssbd2022.ssbd03.mappers.json.LocaleSerializerDeserializer;
import pl.lodz.p.it.ssbd2022.ssbd03.validation.*;

import java.util.Locale;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterClientDto {

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

    @PhoneNumber
    private String phoneNumber;

    @Pesel
    private String pesel;

    @Url
    private String url;

    @NotNull(message = "server.error.validation.constraints.notNull.language")
    @JsonbTypeSerializer(LocaleSerializerDeserializer.class)
    @JsonbTypeDeserializer(LocaleSerializerDeserializer.class)
    private Locale language;

    private String captcha;

}

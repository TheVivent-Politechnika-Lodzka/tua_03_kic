package pl.lodz.p.it.ssbd2022.ssbd03.mok.dto;

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

    @NotNull
    @Password
    private String password;

    @NotNull
    @Login
    private String login;

    @NotNull
    @Email
    private String email;

    @NotNull
    @FirstName
    private String firstName;

    @NotNull
    @LastName
    private String lastName;

    @NotNull
    @PhoneNumber
    private String phoneNumber;

    @NotNull
    @Pesel
    private String pesel;

    @NotNull
    @JsonbTypeSerializer(LocaleSerializerDeserializer.class)
    @JsonbTypeDeserializer(LocaleSerializerDeserializer.class)
    private Locale language;

}

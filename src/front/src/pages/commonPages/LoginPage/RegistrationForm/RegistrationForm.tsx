import "./style.scss";
import { useState } from "react";
import { useRegisterAccountMutation } from "../../../../api/api";

const RegistrationForm = () => {
  const [register] = useRegisterAccountMutation();
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [login, setLogin] = useState("");
  const [pesel, setPesel] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [password, setPassword] = useState("");
  const [repeatPassword, setRepeatPassword] = useState("");

  const handleSubmit = async (event: any) => {
    event.preventDefault();

    const registration = await register({
      email,
      firstName,
      lastName,
      login,
      pesel,
      phoneNumber,
      password,
    });
  };

  return (
    <div className="register">
      <div className="title_text">Zarejestruj</div>
      <div className="input_box">
        <div className="form_group field">
          <input
            type="email"
            className="form_field"
            placeholder="E-mail"
            name="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <label className="form_label">E-mail</label>
        </div>
        <div className="form_group field">
          <input
            type="firstName"
            className="form_field"
            placeholder="Imię"
            name="firstName"
            id="firstName"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            required
          />
          <label className="form_label">Imię</label>
        </div>

        <div className="form_group field">
          <input
            type="lastName"
            className="form_field"
            placeholder="Nazwisko"
            name="lastName"
            id="lastName"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
            required
          />
          <label className="form_label">Nazwisko</label>
        </div>

        <div className="form_group field">
          <input
            type="text"
            className="form_field"
            placeholder="Login"
            name="login"
            id="login"
            value={login}
            onChange={(e) => setLogin(e.target.value)}
            required
          />
          <label className="form_label">Login</label>
        </div>
        <div className="form_group field">
          <input
            type="text"
            className="form_field"
            placeholder="PESEL"
            name="pesel"
            id="pesel"
            value={pesel}
            onChange={(e) => setPesel(e.target.value)}
            required
          />
          <label className="form_label">PESEL</label>
        </div>

        <div className="form_group field">
          <input
            type="text"
            className="form_field"
            placeholder="Numer telefonu"
            name="phoneNumber"
            id="phoneNumber"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            required
          />
          <label className="form_label">Numer telefonu</label>
        </div>

        <div className="form_group field">
          <input
            type="password"
            className="form_field"
            placeholder="Hasło"
            name="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <label className="form_label">Hasło</label>
        </div>

        <div className="form_group field">
          <input
            type="password"
            className="form_field"
            placeholder="Powtórz hasło"
            name="repeatPassword"
            id="repeatPassword"
            value={repeatPassword}
            onChange={(e) => setRepeatPassword(e.target.value)}
            required
          />
          <label className="form_label">Powtórz hasło</label>
        </div>
      </div>

      <div className="register_button" onClick={handleSubmit}>
        ZAREJESTRUJ
      </div>
    </div>
  );
};

export default RegistrationForm;

import styles from "./registrationForm.module.scss";
import { useState } from "react";
import { useRegisterAccountMutation } from "../../../../api/api";
import { useTranslation } from "react-i18next";

const RegistrationForm = () => {
  const [register] = useRegisterAccountMutation();
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [login, setLogin] = useState("");
  const [pesel, setPesel] = useState("");
  const [phone_number, setPhoneNumber] = useState("");
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
      phoneNumber: phone_number,
      password,
      language: {
        language: "pl",
      },
    });

    if ("data" in registration) {
      alert(registration.data);
    }
  };

  const { t } = useTranslation();

  return (
    <div className={styles.register}>
      <div className={styles.title_text}>{t("sign_in")}</div>
      <div className={styles.input_box}>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="email"
            className={styles.form_field}
            placeholder="E-mail"
            name="email"
            id="email"
            value={email}
            onChange={(e: any) => setEmail(e.target.value)}
            required
          />
          <label className={styles.form_label}>E-mail*</label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="firstName"
            className={styles.form_field}
            placeholder={t("first_name")}
            name="firstName"
            id="firstName"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            required
          />
          <label className={styles.form_label}>{t("first_name")}</label>
        </div>

        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="lastName"
            className={styles.form_field}
            placeholder={t("last_name")}
            name="lastName"
            id="lastName"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
            required
          />
          <label className={styles.form_label}>{t("last_name")}</label>
        </div>

        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="text"
            className={styles.form_field}
            placeholder="Login"
            name="login"
            id="login"
            value={login}
            onChange={(e: any) => setLogin(e.target.value)}
            required
          />
          <label className={styles.form_label}>Login*</label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="text"
            className={styles.form_field}
            placeholder="PESEL"
            name="pesel"
            id="pesel"
            value={pesel}
            onChange={(e: any) => setPesel(e.target.value)}
            required
          />
          <label className={styles.form_label}>PESEL*</label>
        </div>

        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="text"
            className={styles.form_field}
            placeholder={t("phone_number")}
            name="phoneNumber"
            id="phoneNumber"
            value={phone_number}
            onChange={(e: any) => setPhoneNumber(e.target.value)}
            required
          />
          <label className={styles.form_label}>{t("phone_number")}</label>
        </div>

        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="password"
            className={styles.form_field}
            placeholder={t("password*")}
            name="password"
            id="password_register"
            value={password}
            onChange={(e: any) => setPassword(e.target.value)}
            required
          />
          <label className={styles.form_label}>{t("password*")}</label>
        </div>

        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="password"
            className={styles.form_field}
            placeholder={t("rpassword")}
            name="repeatPassword"
            id="repeatPassword"
            value={repeatPassword}
            onChange={(e: any) => setRepeatPassword(e.target.value)}
            required
          />
          <label className={styles.form_label}>{t("rpassword")}</label>
        </div>
      </div>

      <div className={styles.register_button} onClick={handleSubmit}>
        {t("sign_in")}
      </div>
    </div>
  );
};

export default RegistrationForm;

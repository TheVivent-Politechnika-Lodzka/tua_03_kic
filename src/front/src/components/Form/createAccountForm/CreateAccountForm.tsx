import { useState } from "react";
import { useTranslation } from "react-i18next";
import { useCreateAccountMutation } from "../../../api/api";
import styles from "./createAccountForm.module.scss";
import { Navigate } from "react-router-dom";

export default function CreateAccountForm() {
  const [create] = useCreateAccountMutation();
  const [email, setEmail] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [lenguageFromForm, setLenguageFromForm] = useState("pl");

  const handleSubmit = async (event: any) => {
    event.preventDefault();

    const createAccount = await create({
      email,
      firstName,
      lastName,
      login,
      password,
      language: {
        language: lenguageFromForm,
      },
    });

    if ("data" in createAccount) {
      alert("Konto zostałol utworzone");
    } else if ("error" in createAccount && "status" in createAccount.error) {
      alert(createAccount.error.data);
      console.log({ createAccount });
    } else {
      alert("Wystąpił błąd");
    }
    <Navigate to="/admin" />;
  };

  const { t } = useTranslation();

  return (
    <div className={styles.create_account}>
      <div className={styles.title_text}>
        {t("CreateAccountForm.form_title")}
      </div>
      <div className={styles.input_box}>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type={"email"}
            className={styles.form_field}
            placeholder={"E-mail"}
            name={"emailToCreate"}
            id={"emailToCreate"}
            required
            value={email}
            onChange={(e: any) => setEmail(e.target.value)}
          />
          <label className={styles.form_label}>
            {t("CreateAccountForm.email")}
          </label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type={"firstName"}
            className={styles.form_field}
            placeholder={"firstName"}
            name={"firstNameToCreate"}
            id={"firstNameToCreate"}
            required
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
          <label className={styles.form_label}>
            {t("CreateAccountForm.first_name")}
          </label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type={"lastName"}
            className={styles.form_field}
            placeholder={"lastName"}
            name={"lastNameToCreate"}
            id={"lastNameToCreate"}
            required
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
          <label className={styles.form_label}>
            {t("CreateAccountForm.last_name")}
          </label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type={"login"}
            className={styles.form_field}
            placeholder={"login"}
            name={"loginToCreate"}
            id={"loginToCreate"}
            required
            value={login}
            onChange={(e: any) => setLogin(e.target.value)}
          />
          <label className={styles.form_label}>
            {t("CreateAccountForm.login")}
          </label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type={"password"}
            className={styles.form_field}
            placeholder={"password*"}
            name={"passwordToCreate"}
            id={"passwordToCreate"}
            required
            value={password}
            onChange={(e: any) => setPassword(e.target.value)}
          />
          <label className={styles.form_label}>
            {t("CreateAccountForm.password")}
          </label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <select
            id="lenguageToCreate"
            name="lenguageToCreate"
            value={lenguageFromForm}
            required
            onChange={(e: any) => setLenguageFromForm(e.target.value)}
          >
            <option value="pl" id="pl">
              pl
            </option>
            <option value="en" id="en">
              en
            </option>
          </select>
          <label className={styles.form_label}>
            {t("CreateAccountForm.lenguage")}
          </label>
        </div>
      </div>
      <div className={styles.create_account_button} onClick={handleSubmit}>
        {t("CreateAccountForm.submit")}
      </div>
    </div>
  );
}

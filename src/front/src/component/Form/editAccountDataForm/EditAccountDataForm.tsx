import styles from "./editAccountDataForm.module.scss";
import { useState, useEffect } from "react";
import { AccountWithAccessLevelsDto } from "../../../api/types/apiParams";
import { useTranslation } from "react-i18next";
import {
  useEditOtherAccountDataMutation,
  useGetAccountByLoginMutation,
} from "../../../api/api";

const EditAccountDataForm = () => {
  const [edit] = useEditOtherAccountDataMutation();

  const [login, setLogin] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");

  const [message, setMessage] = useState("");

  const [data, setData] = useState<AccountWithAccessLevelsDto>(
    {} as AccountWithAccessLevelsDto
  );

  const [getUser, { isLoading }] = useGetAccountByLoginMutation();

  const { t } = useTranslation();

  const handleSubmit = async (event: any) => {
    event.preventDefault();
    const response = await getUser(login);

    if ("data" in response) {
      setData(response.data);
    }

    const internalData = {
      ...data,
      firstName,
      lastName,
    };

    const internalUserData = {
      login,
      data: internalData,
    };

    const res = await edit(internalUserData);

    if ("data" in res) {
    } else if ("error" in res && "status" in res.error) {
      if (res.error.status === "PARSING_ERROR")
        setMessage(res.error.data as string);
    } else setMessage(t("server_error"));
  };

  return (
    <div className={styles.login_left}>
      <div className={styles.title_text}>{t("edit_details")}</div>
      <div className={styles.input_box}>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="login"
            className={styles.form_field}
            placeholder="Login"
            name="login"
            id="login"
            // value={userData.login}
            onChange={(e) => setLogin(e.target.value)}
            required
          />
          <label className={styles.form_label}>Login</label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="firstName"
            className={styles.form_field}
            placeholder={t("first_name_edit")}
            name="firstName"
            id="firstName"
            // value={userData.data.firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />
          <label className={styles.form_label}>{t("first_name_edit")}</label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="firstName"
            className={styles.form_field}
            placeholder={t("last_name_edit")}
            name="firstName"
            id="firstName"
            // value={userData.data.lastName}
            onChange={(e) => setLastName(e.target.value)}
          />
          <label className={styles.form_label}>{t("last_name_edit")}</label>
        </div>

        <div className={styles.remove_button} onClick={handleSubmit}>
          {t("edit")}
        </div>
      </div>
      <div className={styles.message_text}>{message}</div>
    </div>
  );
};

export default EditAccountDataForm;

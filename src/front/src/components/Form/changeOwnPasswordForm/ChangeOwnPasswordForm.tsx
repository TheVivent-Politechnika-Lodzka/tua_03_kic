import styles from "./changeOwnPasswordForm.module.scss";
import { useState } from "react";
import { useChangeOwnPasswordMutation } from "../../../api/api";
import { useTranslation } from "react-i18next";
import { useGoogleReCaptcha } from "react-google-recaptcha-v3";

interface LoginFormProps {
  ETag: string;
}

const ChangeOwnPasswordForm = ({ ETag }: LoginFormProps) => {
  const [change, { isLoading }] = useChangeOwnPasswordMutation();
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [message, setMessage] = useState("");
  const { t } = useTranslation();

  const { executeRecaptcha } = useGoogleReCaptcha();

  const handleSubmit = async (event: any) => {
    event.preventDefault();

    if (executeRecaptcha === undefined) {
      return;
    }

    const captcha = await executeRecaptcha("register");

    if (oldPassword && newPassword) {
      const res = await change({
        oldPassword,
        newPassword,
        ETag,
        captcha,
      });

      if ("data" in res) {
      } else if ("error" in res && "status" in res.error) {
        if (res.error.status === "PARSING_ERROR")
          setMessage(res.error.data as string);
      } else setMessage(t("server_error"));
    } else {
      setMessage(t("refill_data"));
    }
  };

  return (
    <div className={styles.login_left}>
      <div className={styles.title_text}>{t("change_password")}</div>
      <div className={styles.input_box}>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="password"
            className={styles.form_field}
            placeholder={t("c_password")}
            name="oldPassword"
            value={oldPassword}
            onChange={(e: any) => setOldPassword(e.target.value)}
            required
          />
          <label className={styles.form_label}>{t("c_password")}</label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="password"
            pattern={"^(?=.*[p{Lu}])(?=.*[p{Ll}])(?=.*d).+$"}
            minLength={8}
            className={styles.form_field}
            placeholder={t("n_password")}
            name="newPassword"
            value={newPassword}
            onChange={(e) => setNewPassword(e.target.value)}
            required
          />
          <label className={styles.form_label}>{t("n_password")}</label>
        </div>
      </div>

      <div className={styles.login_button} onClick={handleSubmit}>
        {t("change_password")}
      </div>
      <div className={styles.message_text}>{message}</div>
    </div>
  );
};

export default ChangeOwnPasswordForm;

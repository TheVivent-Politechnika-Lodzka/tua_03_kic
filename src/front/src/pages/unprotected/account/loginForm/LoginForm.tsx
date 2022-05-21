import styles from "./loginForm.module.scss";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useLoginMutation } from "../../../../api/api";
import { login as loginDispatch } from "../../../../redux/userSlice";
import { useTranslation } from "react-i18next";


const LoginForm = () => {
  const [authenticate, { isLoading }] = useLoginMutation();
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const handleSubmit = async (event: any) => {
    event.preventDefault();

    if (login && password) {
      const res = await authenticate({
        login,
        password,
      });

      if ("data" in res) {
        dispatch(loginDispatch(res.data));
        navigate("/");
      } else if ("error" in res && "status" in res.error) {
        if (res.error.status === 401) setMessage(t("wrong_data"));
      } else setMessage(t("server_error"));

    } else {
      setMessage(t("refill_data"));
    }
  };

  return (
    <div className={styles.login_left}>
      <div className={styles.title_text}>{t("log_in")}</div>
      <div className={styles.input_box}>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="input"
            className={styles.form_field}
            placeholder="Login"
            name="login"
            id="login"
            value={login}
            onChange={(e:any) => setLogin(e.target.value)}
            required
          />
          <label className={styles.form_label}>Login</label>
        </div>
        <div className={`${styles.form_group} ${styles.field}`}>
          <input
            type="password"
            className={styles.form_field}
            placeholder={t("password")}
            name="password"
            id="password_login"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <label className={styles.form_label}>{t("password")}</label>
        </div>
      </div>

      <div className={styles.login_button} onClick={handleSubmit}>
        {t("log_in")}
      </div>
      <div className={styles.message_text}>{message}</div>
    </div>
  );
};

export default LoginForm;

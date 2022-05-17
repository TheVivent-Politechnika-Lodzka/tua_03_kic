import "./style.scss";
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
      const decoded = await authenticate({
        login,
        password,
      });

      // TODO poprawić parsowanie

      if (JSON.parse(JSON.stringify(decoded)).data) {
        dispatch(loginDispatch(JSON.parse(JSON.stringify(decoded)).data));
        navigate("/", { replace: true });
      } else {
        const res = JSON.parse(JSON.stringify(decoded)).error;
        if (res.status === 401) setMessage(t("wrong_data"));
        else setMessage(t("server_error"));
      }
    } else {
      setMessage(t("refill_data"));
    }
  };

  return (
    <div className="login_left">
      <div className="title_text">{t("log_in")}</div>
      <div className="input_box">
        <div className="form_group field">
          <input
            type="input"
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
            type="password"
            className="form_field"
            placeholder={t("password")}
            name="password"
            id="password_login"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <label className="form_label">{t("password")}</label>
        </div>
      </div>

      <div className="login_button" onClick={handleSubmit}>
        {t("log_in")}
      </div>
      <div className="message_text">{message}</div>
    </div>
  );
};

export default LoginForm;

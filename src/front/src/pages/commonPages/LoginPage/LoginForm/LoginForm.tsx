import "./style.scss";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useLoginMutation } from "../../../../api/api";
import { login as loginDispatch } from "../../../../redux/userSlice";

const LoginForm = () => {
  const [authenticate, { isLoading }] = useLoginMutation();
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

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
        if (res.status === 401) setMessage("Niepoprawne dane logowania");
        else setMessage("Błąd serwera, spróbuj za chwilę");
      }
    } else {
      setMessage("Uzupełnij dane logowania");
    }
  };

  return (
    <div className="login_left">
      <div className="title_text">Zaloguj się</div>
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
            placeholder="Hasło"
            name="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          <label className="form_label">Hasło</label>
        </div>
      </div>

      <div className="login_button" onClick={handleSubmit}>
        ZALOGUJ
      </div>
      <div className="message_text">{message}</div>
    </div>
  );
};

export default LoginForm;

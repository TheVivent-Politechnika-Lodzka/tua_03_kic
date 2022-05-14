import { useState } from "react";
import { useDispatch } from "react-redux";
import { Link, useNavigate } from "react-router-dom";
import { useLoginMutation } from "../../../api/api";
import TopBar from "../../../component/TopBar/SelectorTopBar";
import { useStoreSelector } from "../../../redux/reduxHooks";
import { login as loginDispatch } from "../../../redux/userSlice";
import "./style.scss";

const LoginPage = () => {
  const [authenticate, { isLoading }] = useLoginMutation();
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useStoreSelector((state) => state.user);

  const handleSubmit = async (event: any) => {
    event.preventDefault();

    if (login && password) {
      const decoded = await authenticate({
        login,
        password,
      });

      dispatch(loginDispatch(decoded));
      if (user.sub !== "") {
        navigate("/", { replace: true });
      } else {
        setMessage("Niepoprawne dane");
      }
    } else {
      setMessage("Uzupełnij dane logowania");
    }
  };

  return (
    <div className="page">
      <div className="logo" onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className="login_section">
        <div className="login_left">
          <div className="titletext">Zaloguj się</div>
          <div className="login_box">
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
                  placeholder="Password"
                  name="password"
                  id="password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
                <label className="form_label">Password</label>
              </div>
            </div>
          </div>
          <div className="login_button" onClick={handleSubmit}>
            ZALOGUJ
          </div>
          <div className="message_text">{message}</div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;

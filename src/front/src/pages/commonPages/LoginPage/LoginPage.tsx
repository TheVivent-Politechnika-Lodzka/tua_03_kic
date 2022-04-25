import { useState } from "react";
import "./style.scss";
import { Link } from "react-router-dom";

const LoginPage = () => {
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [token, setToken] = useState("");

  const handleSubmit = async (event: any) => {
    event.preventDefault();
    const response = await fetch(
      // "http://studapp.it.p.lodz.pl:8003/api/mok/login",
      "http://localhost:8080/api/mok/login",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          login,
          password,
        }),
      }
    );

    const data = await response.text();
    setToken(data);
  };

  return (
    <div>
      <div>
        <Link to="/" style={{ textDecoration: "none" }}>
          <img src="./../logo.jpg" alt="Logo" height="80px" />
        </Link>
      </div>
      <div className="login_section">
        <div className="login_left">
          <div className="titletext">Zaloguj się</div>
          <div className="login_box">
            <div className="description_box">
              <div>
                <label>Login lub mail</label>
              </div>
              <div>
                <label>Haslo</label>
              </div>
            </div>
            <div className="input_box">
              <div>
                <input value={login} onChange={(e)=> setLogin(e.target.value)}></input>
              </div>
              <div>
                <input value={password} onChange={(e)=> setPassword(e.target.value)}></input>
              </div>
            </div>
          </div>
          <div className="login_button" onClick={handleSubmit}>Zaloguj</div>
          <div>{token}</div>
        </div>

        <div className="login_right">
          <div className="titletext">Nie masz jeszcze konta ?</div>
          <div className="undertitletext">Zarejestruj się</div>
          <div className="login_box">
            <div className="description_box">
              <div>
                <label>Mail</label>
              </div>
              <div>
                <label>Imie</label>
              </div>
              <div>
                <label>Nazwisko</label>
              </div>
              <div>
                <label>Login</label>
              </div>
              <div>
                <label>PESEL</label>
              </div>
              <div>
                <label>Hasło</label>
              </div>
              <div>
                <label>Powtorz hasło</label>
              </div>
            </div>
            <div className="input_box">
              <div>
                <input></input>
              </div>
              <div>
                <input></input>
              </div>
              <div>
                <input></input>
              </div>
              <div>
                <input></input>
              </div>
              <div>
                <input></input>
              </div>
              <div>
                <input></input>
              </div>
              <div>
                <input></input>
              </div>
            </div>

            <div className="login_button">Zarejestruj</div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;

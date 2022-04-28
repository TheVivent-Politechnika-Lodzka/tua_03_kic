import { useState } from "react";
import { Button, Form } from "react-bootstrap";
//import "bootstrap/dist/css/bootstrap.min.css";
import "./style.scss";

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
        <div className="text_token">
          {token ? "Token: " + token : "Zaloguj się"}
        </div>
        <Form onSubmit={handleSubmit}>
          <Form.Group className="text_token" controlId="formBasicEmail">
            <Form.Label>Login</Form.Label>
            <Form.Control
              type="text"
              placeholder="Login"
              value={login}
              onChange={(e) => setLogin(e.target.value)}
            />
          </Form.Group>

          <Form.Group className="text_token" controlId="formBasicPassword">
            <Form.Label>Password</Form.Label>
            <Form.Control
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </Form.Group>
          <Button variant="primary" type="submit">
            Submit
          </Button>
        </Form>
      </div>
    </div>
  );
};

export default LoginPage;

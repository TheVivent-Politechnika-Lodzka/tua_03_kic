import jwtDecode from "jwt-decode";
import { useState } from "react";
import { Button, Form } from "react-bootstrap";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { useLoginMutation } from "../../../api/api";
import { AccountDto } from "../../../api/types/mok.dto";
import { login as loginDispatch } from "../../../redux/userSlice";
import "./style.scss";

const LoginPage = () => {
  const [authenticate, { isLoading }] = useLoginMutation();
  const [login, setLogin] = useState("");
  const [password, setPassword] = useState("");
  const [token, setToken] = useState("");

  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleSubmit = async (event: any) => {
    event.preventDefault();
    const decoded = await authenticate({
      login,
      password,
    });
    dispatch(loginDispatch(decoded));
    navigate("/", { replace: true });
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

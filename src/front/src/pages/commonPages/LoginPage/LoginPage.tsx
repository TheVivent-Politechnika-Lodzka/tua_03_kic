import { useNavigate } from "react-router-dom";
import LoginForm from "./LoginForm/LoginForm";
import RegistrationForm from "./RegistrationForm/RegistrationForm";
import "./style.scss";

const LoginPage = () => {
  const navigate = useNavigate();
  return (
    <div >
      <div  onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className="login_section">
        <LoginForm />
        <RegistrationForm />
      </div>
    </div>
  );
};

export default LoginPage;

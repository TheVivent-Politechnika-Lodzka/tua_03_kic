import { useNavigate } from "react-router-dom";
import LangSelect from "../../../../component/LangSelect/LangSelect";
import LoginForm from "../loginForm/LoginForm";
import RegistrationForm from "../registerForm/RegistrationForm";
import "./style.scss";

const LoginPage = () => {
  const navigate = useNavigate();
  return (
    <div >
      <div  onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <LangSelect/>
      <div className="login_section">
        <LoginForm />
        <RegistrationForm />
      </div>
    </div>
  );
};

export default LoginPage;

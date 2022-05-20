import { useNavigate } from "react-router-dom";
import LangSelect from "../../../../component/Selector/LangBarSelect/LangSelect";
import LoginForm from "../loginForm/LoginForm";
import RegistrationForm from "../registerForm/RegistrationForm";
import styles from "./loginPage.module.scss";

const LoginPage = () => {
  const navigate = useNavigate();
  return (
    <div>
      <div onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <LangSelect />
      <div className={styles.login_section}>
        <LoginForm />
        <RegistrationForm />
      </div>
    </div>
  );
};

export default LoginPage;

import Logo from "../../../components/Logo/Logo";
import LangSelect from "../../../components/Selector/LangBarSelect/LangSelect";
import LoginForm from "../../../components/Form/loginForm/LoginForm";
import RegistrationForm from "../../../components/Form/registerForm/RegistrationForm";
import styles from "./registerPage.module.scss";
import { GoogleReCaptchaProvider } from "react-google-recaptcha-v3";

const RegisterPage = () => {
  return (
    <div>
      <Logo />
      <LangSelect />
      <div className={styles.login_section}>
        <LoginForm />
        <GoogleReCaptchaProvider reCaptchaKey="6Lf85hEgAAAAAONrGfo8SoQSb9GmfzHXTTgjKJzT">
          <RegistrationForm />
        </GoogleReCaptchaProvider>
      </div>
    </div>
  );
};

export default RegisterPage;

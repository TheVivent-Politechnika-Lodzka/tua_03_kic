import Logo from "../../../component/Logo/Logo";
import LangSelect from "../../../component/Selector/LangBarSelect/LangSelect";
import LoginForm from "../../../component/Form/loginForm/LoginForm";
import RegistrationForm from "../../../component/Form/registerForm/RegistrationForm";
import styles from "./loginPage.module.scss";
import { GoogleReCaptchaProvider } from "react-google-recaptcha-v3";

const LoginPage = () => {
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

export default LoginPage;

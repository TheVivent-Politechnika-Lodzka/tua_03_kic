import Logo from "../../../component/Logo/Logo";
import LangSelect from "../../../component/Selector/LangBarSelect/LangSelect";
import LoginForm from "../../../component/Form/loginForm/LoginForm";
import RegistrationForm from "../../../component/Form/registerForm/RegistrationForm";
import styles from "./loginPage.module.scss";

const LoginPage = () => {
  return (
    <div>
      <Logo />
      <LangSelect />
      <div className={styles.login_section}>
        <LoginForm />
        <RegistrationForm />
      </div>
    </div>
  );
};

export default LoginPage;

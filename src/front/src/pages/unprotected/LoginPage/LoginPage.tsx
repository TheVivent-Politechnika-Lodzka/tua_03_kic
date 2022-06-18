import { faSignIn } from "@fortawesome/free-solid-svg-icons";
import { Checkbox } from "@mantine/core";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import background from "../../../assets/images/loginPageBackground.jpg";
import ActionButton from "../../../components/shared/ActionButton/ActionButton";
import Input from "../../../components/shared/Input/Input";
import styles from "./style.module.scss";

const LoginPage = () => {
    const [credentials, setCredentials] = useState<Credentials>({
        login: "",
        password: "",
    });

    const { t } = useTranslation();
    return (
        <section className={styles.login_page}>
            <div className={styles.background} />
            <div className={styles.form_wrapper}>
                <h2 className={styles.header}>Zaloguj się</h2>
                <div className={styles.input_wrapper}>
                    <Input
                        title="Login*"
                        placeholder="Wpisz swój login"
                        type="text"
                        value={credentials.login}
                        onChange={(e) => {
                            setCredentials({
                                ...credentials,
                                login: e.target.value,
                            });
                        }}
                    />
                    <Input
                        title={t("password*")}
                        placeholder="Wpisz swoje hasło"
                        type="password"
                        value={credentials.password}
                        onChange={(e) => {
                            setCredentials({
                                ...credentials,
                                password: e.target.value,
                            });
                        }}
                    />
                </div>
                <div className={styles.action_button_wrapper}>
                    <ActionButton
                        title="Zaloguj się"
                        onClick={() => {}}
                        icon={faSignIn}
                        color="purple"
                    />
                    <p className={styles.forgot_password}>Zapomniałem hasła</p>
                </div>
            </div>
        </section>
    );
};

export default LoginPage;

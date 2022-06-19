import { faSignIn } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useDispatch } from "react-redux";
import { useNavigate } from "react-router";
import { login, resetPassword } from "../../../api";
import { login as loginDispatch } from "../../../redux/userSlice";
import ActionButton from "../../../components/shared/ActionButton/ActionButton";
import Input from "../../../components/shared/Input/Input";
import { failureNotificationItems } from "../../../utils/showNotificationsItems";
import styles from "./style.module.scss";
import jwtDecode from "jwt-decode";
import { ResetPasswordModal } from "./ResetPasswordModal";

const LoginPage = () => {
    const accessToken = "ACCESS_TOKEN";

    const [credentials, setCredentials] = useState<Credentials>({
        login: "",
        password: "",
    });
    const [loading, setLoading] = useState<Loading>({
        pageLoading: false,
        actionLoading: false,
    });
    const [isResetPasswordModalOpen, setResetPasswordModalOpen] =
        useState(false);

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { t } = useTranslation();

    const handleLogin = async () => {
        if (
            credentials.login.length === 0 ||
            credentials.password.length === 0
        ) {
            setLoading({ ...loading, actionLoading: false });
            showNotification(
                failureNotificationItems(
                    "Dane uwierzytelniające nie mogą być puste!"
                )
            );
            return;
        }
        setLoading({ ...loading, actionLoading: true });
        const response = await login(credentials.login, credentials.password);
        if ("errorMessage" in response) {
            setLoading({ ...loading, actionLoading: false });
            showNotification(failureNotificationItems(response?.errorMessage));
            return;
        }
        const decodedJWT = jwtDecode(response.accessToken) as string;
        dispatch(loginDispatch(decodedJWT));
        localStorage.setItem(accessToken, response.accessToken);
        setLoading({ ...loading, actionLoading: false });
        navigate("/");
    };
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
                        onChange={(e) =>
                            setCredentials({
                                ...credentials,
                                login: e.target.value,
                            })
                        }
                    />
                    <Input
                        title={t("password*")}
                        placeholder="Wpisz swoje hasło"
                        type="password"
                        value={credentials.password}
                        onChange={(e) =>
                            setCredentials({
                                ...credentials,
                                password: e.target.value,
                            })
                        }
                    />
                </div>
                <div className={styles.action_button_wrapper}>
                    <ActionButton
                        isLoading={loading.actionLoading}
                        title="Zaloguj się"
                        onClick={handleLogin}
                        icon={faSignIn}
                        color="purple"
                    />
                    <p
                        onClick={() => {
                            setResetPasswordModalOpen(true);
                        }}
                        className={styles.forgot_password}
                    >
                        Zapomniałem hasła
                    </p>
                </div>
            </div>
            <ResetPasswordModal
                isOpen={isResetPasswordModalOpen}
                onClose={() => {
                    setResetPasswordModalOpen(false);
                }}
            />
        </section>
    );
};

export default LoginPage;

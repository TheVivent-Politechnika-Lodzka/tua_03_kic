import styles from "./resetPasswordToken.module.scss"
import {useResetPasswordChangeMutation} from "../../../api/api";
import {useState} from "react";
import {useTranslation} from "react-i18next";

const ResetPasswordTokenForm = () => {
    const[resetPassword] = useResetPasswordChangeMutation();
    const[login, setLogin] = useState("");
    const[newPassword, setNewPassword] = useState("");
    const[confirmNewPassword, setConfirmNewPassword] = useState("");
    const[token, setToken] = useState("");
    const[message, setMessage] = useState("");

    const { t } = useTranslation();

    const handleSubmit = async (event: any) => {
        event.preventDefault();

        if(newPassword == confirmNewPassword) {
            const reset = await resetPassword({
                login,
                token,
                password: newPassword
            });

            if ("data" in reset) {
                setMessage(t("password_changed"));
            } else {
                setMessage(t("wrong_data"));
            }
        } else {
            setMessage(t("wrong_data"));
        }

    };

    return (
        <div className={styles.reset}>
            <div className={styles.title_text}>{t("reset_password")}</div>
            <div className={styles.input_box}>
                <div className={`${styles.form_group} ${styles.field}`}>
                    <input
                        type="input"
                        className={styles.form_field}
                        placeholder="Login"
                        name="login"
                        id="login"
                        value={login}
                        onChange={(e: any) => setLogin(e.target.value)}
                        required
                    />
                    <label className={styles.form_label}>Login</label>
                </div>
                <div className={`${styles.form_group} ${styles.field}`}>
                    <input
                        type="password"
                        className={styles.form_field}
                        placeholder={t("password")}
                        name="password"
                        id="password"
                        value={newPassword}
                        onChange={(e: any) => setNewPassword(e.target.value)}
                        required
                    />
                    <label className={styles.form_label}>{t("password")}</label>
                </div>
                <div className={`${styles.form_group} ${styles.field}`}>
                    <input
                        type="password"
                        className={styles.form_field}
                        placeholder={t("rpassword")}
                        name="cpassword"
                        id="cpassword"
                        value={confirmNewPassword}
                        onChange={(e: any) => setConfirmNewPassword(e.target.value)}
                        required
                    />
                    <label className={styles.form_label}>{t("rpassword")}</label>
                </div>
                <div className={`${styles.form_group} ${styles.field}`}>
                    <input
                        type="input"
                        className={styles.form_field}
                        placeholder="Token"
                        name="token"
                        id="token"
                        value={token}
                        onChange={(e: any) => setToken(e.target.value)}
                        required
                    />
                    <label className={styles.form_label}>Token</label>
                </div>
            </div>
            <div className={styles.reset_button} onClick={handleSubmit}>
                {t("reset_password")}
            </div>
            <div className={styles.message_text}>{message}</div>
        </div>
    );

}

export default ResetPasswordTokenForm;
import styles from "./resetPassword.module.scss";
import {useResetPasswordRequestMutation} from "../../../api/api";
import {useState} from "react";
import {useTranslation} from "react-i18next";


const ResetPasswordForm = () => {
    const[sendEmail] = useResetPasswordRequestMutation();
    const[login, setLogin] = useState("");
    const[message, setMessage] = useState("");

    const handleSubmit = async (event: any) => {
        event.preventDefault();

        const sended = await sendEmail(login);

        if ("data" in sended) {
            setMessage(t("email_send"));
        } else {
            setMessage(t("email_error"));
        }
    };

    const { t } = useTranslation();

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
            </div>
            <div className={styles.reset_button} onClick={handleSubmit}>
                {t("reset_password")}
            </div>
            <div className={styles.message_text}>{message}</div>
        </div>
    );
};

export default ResetPasswordForm;
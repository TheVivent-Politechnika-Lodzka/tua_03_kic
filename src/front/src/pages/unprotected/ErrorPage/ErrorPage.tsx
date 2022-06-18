import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
import robot from "../../../assets/robot.gif";
import styles from "./style.module.scss";

const ErrorPage = () => {
    const { t } = useTranslation();

    const navigate = useNavigate();
    return (
        <section className={styles.error_page}>
            <div className={styles.errorbox_wrapper}>
                <img className={styles.robot} src={robot} alt="robot" />
                <div className={styles.errortext_wrapper}>
                    <div className={styles.errortext}>{t("error_page")}</div>
                    <div className={styles.linkerror}>
                        {t("invite_main_page")}
                    </div>
                    <div
                        className={styles.button}
                        onClick={() => navigate("/")}
                    >
                        {t("main_page")}
                    </div>
                </div>
            </div>
        </section>
    );
};

export default ErrorPage;

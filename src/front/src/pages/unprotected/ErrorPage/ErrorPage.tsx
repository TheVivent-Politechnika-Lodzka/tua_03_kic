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
                    <div className={styles.errortext}>
                        {t("errorPage.errorPage")}
                    </div>
                    <div className={styles.linkerror}>
                        {t("errorPage.inviteMainPage")}
                    </div>
                    <div
                        className={styles.button}
                        onClick={() => navigate("/")}
                    >
                        {t("errorPage.mainPage")}
                    </div>
                </div>
            </div>
        </section>
    );
};

export default ErrorPage;

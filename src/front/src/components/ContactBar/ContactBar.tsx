import { useTranslation } from "react-i18next";
import { FaPhoneAlt, FaInstagram, FaFacebook } from "react-icons/fa";
import { HiOutlineMailOpen } from "react-icons/hi";
import styles from "./style.module.scss";

const ContactBar = () => {
    const { t } = useTranslation();

    return (
        <div className={styles.contact_section}>
            <div className={styles.contact}>
                <div className={styles.contact_section_title1}>
                    {t("contentBar.contact")}
                </div>
                <div className={styles.contact_section_items}>
                    <div className={styles.contact_section_item}>
                        <FaPhoneAlt className={styles.contact_icon} />
                        <p>{t("contentBar.tel")}</p>
                    </div>
                    <div className={styles.contact_section_item}>
                        <HiOutlineMailOpen className={styles.contact_icon} />
                        <p>{t("contentBar.email")}</p>
                    </div>
                </div>
            </div>
            <div className={styles.social_media}>
                <div className={styles.contact_section_title2}>
                    {t("contentBar.socialMedia")}
                </div>
                <div className={styles.contact_section_items}>
                    <div className={styles.contact_section_item}>
                        <FaFacebook className={styles.contact_icon} />
                        <p>{t("contentBar.kic")}</p>
                    </div>
                    <div className={styles.contact_section_item}>
                        <FaInstagram className={styles.contact_icon} />
                        <p>{t("contentBar.kicTwo")}</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ContactBar;

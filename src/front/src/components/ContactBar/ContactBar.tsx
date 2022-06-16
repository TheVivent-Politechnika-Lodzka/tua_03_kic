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
                    {t("contact")}
                </div>
                <div className={styles.contact_section_items}>
                    <div className={styles.contact_section_item}>
                        <FaPhoneAlt className={styles.contact_icon} />
                        <p>+48 303 030 300</p>
                    </div>
                    <div className={styles.contact_section_item}>
                        <HiOutlineMailOpen className={styles.contact_icon} />
                        <p>KIC@kic.agency</p>
                    </div>
                </div>
            </div>
            <div className={styles.social_media}>
                <div className={styles.contact_section_title2}>
                    {t("social_media")}
                </div>
                <div className={styles.contact_section_items}>
                    <div className={styles.contact_section_item}>
                        <FaFacebook className={styles.contact_icon} />
                        <p>KIC</p>
                    </div>
                    <div className={styles.contact_section_item}>
                        <FaInstagram className={styles.contact_icon} />
                        <p>@KIC</p>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default ContactBar;

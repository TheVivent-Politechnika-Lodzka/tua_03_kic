import ContactBar from "../../../components/ContactBar/ContactBar";
import Slider from "../../../components/Slider/Slider";
import styles from "./style.module.scss";
import { useTranslation } from "react-i18next";

const HomePage = () => {
    const { t } = useTranslation();

    return (
        <div>
            <Slider />

            <section className={styles.section_content}>
                <div className={styles.title}>{t("homePage.title")}</div>
                <div className={styles.second_title}>
                    {t("homePage.secondTitle")}"
                </div>
                <div className={styles.items_list}>
                    <div className={styles.single_item_circle}>
                        <div className={styles.item_image}>
                            <img src="brain.jpg" alt="brain.jpg" />
                        </div>
                        <div className={styles.text_box}>
                            <div className={styles.text_square_title}>
                                {t("homePage.ownLab")}
                            </div>
                            <div className={styles.text_square}>
                                {t("homePage.loremIpsum")}
                            </div>
                        </div>
                    </div>
                    <div className={styles.single_item_circle}>
                        <div className={styles.item_image}>
                            <img src="doctor.jpg" alt="doctor" />
                        </div>
                        <div className={styles.text_box}>
                            <div className={styles.text_square_title}>
                                {t("homePage.bestSpecialist")}
                            </div>
                            <div className={styles.text_square}>
                                {t("homePage.loremIpsum")}
                            </div>
                        </div>
                    </div>
                    <div className={styles.single_item_circle}>
                        <div className={styles.item_image}>
                            <img src="klient.jpg" alt="klient" />
                        </div>
                        <div className={styles.text_box}>
                            <div className={styles.text_square_title}>
                                {t("homePage.satisfiedClients")}
                            </div>
                            <div className={styles.text_square}>
                                {t("homePage.loremIpsum")}
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <section className={styles.section_content}>
                <div className={styles.title}> {t("homePage.ourClinic")}</div>
                <div className={styles.second_title}>
                    {t("homePage.goodLocation")}
                </div>
                <div className={styles.items_list}>
                    <div className={styles.card}>
                        <div className={styles.item_wrapper}>
                            <img src="klinika1.jpg" alt="klinika1" />
                            <div className={styles.city_name}>
                                {t("homePage.kicOne.city")}
                            </div>
                            <div className={styles.street_name}>
                                {t("homePage.kicOne.street")}{" "}
                            </div>
                        </div>
                    </div>
                    <div className={styles.card}>
                        <div className={styles.item_wrapper}>
                            <img src="klinika2.jpg" alt="klinika2" />
                            <div className={styles.city_name}>
                                {t("homePage.kicTwo.city")}
                            </div>
                            <div className={styles.street_name}>
                                {t("homePage.kicTwo.street")}{" "}
                            </div>
                        </div>
                    </div>
                    <div className={styles.card}>
                        <div className={styles.item_wrapper}>
                            <img src="klinika3.jpg" alt="klinika3" />
                            <div className={styles.city_name}>
                                {t("homePage.kicThree.city")}
                            </div>
                            <div className={styles.street_name}>
                                {t("homePage.kicThree.street")}
                            </div>
                        </div>
                    </div>
                </div>
            </section>
            <ContactBar />
        </div>
    );
};

export default HomePage;

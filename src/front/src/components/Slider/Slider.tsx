import { useTranslation } from "react-i18next";
import styles from "./style.module.scss";

const Slider = () => {
    const { t } = useTranslation();

    const scrolldown = () => {
        const slider = document.querySelector("#slider") as HTMLElement | null;
        if (slider) {
            const height = slider.offsetHeight;
            window.scrollTo({
                top: height,
                left: 0,
                behavior: "smooth",
            });
        }
    };

    return (
        <div className={styles.slider_wrapper} id="slider">
            <div className={styles.content_wrapper}>
                <div className={styles.info_wrapper}>
                    <h1 className={styles.firm_name}>{t("firm_name")}</h1>
                    <p className={styles.firm_description}>
                        Lorem ipsum dolor sit amet consectetur, adipisicing
                        elit. Facere, error ipsa inventore voluptatibus non
                        excepturi ea, hic cumque rerum adipisci tempore numquam,
                        doloremque laboriosam. Ea velit iusto ratione iure
                        fugiat.
                    </p>
                    <div className={styles.scroll_button} onClick={scrolldown}>
                        {t("more_inf")}
                    </div>
                </div>
            </div>
            <video
                autoPlay
                loop
                muted
                width="100%"
                height="100%"
                className={styles.clip}
            >
                <source src="main.mp4" type="video/mp4" />
            </video>
        
        </div>
    );
};

export default Slider;

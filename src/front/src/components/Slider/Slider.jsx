import { useTranslation } from "react-i18next";
import styles from "./slider.module.scss";

const Slider = () => {
  const { t } = useTranslation();

  const scrolldown = () => {
    const height = document.querySelector("#sliderdiv").offsetHeight;
    window.scrollTo({
      top: height,
      left: 0,
      behavior: "smooth",
    });
  };

  return (
    <div id="sliderdiv">
      <div className={styles.sliderText}>
        {t("firm_name")}
        <div className={styles.scroll_Button} onClick={scrolldown}>
          {t("more_inf")}
        </div>
      </div>
      <video autoPlay loop muted id="clip" width="100%">
        <source src="main.mp4" type="video/mp4" />
      </video>
    </div>
  );
};

export default Slider;

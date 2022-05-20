import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import ImplantNavBar from "../../NavBar/ImplantNavBar/ImplantNavBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";
import styles from "./guestTopBar.module.scss";

const GuestTopBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  return (
    <div className={styles.topBar}>
      <div
        className={styles.logo}
        onClick={() => navigate("/", { replace: false })}
      >
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className={styles.links}>
        <div className={styles.item}>
          <ImplantNavBar />
        </div>
        <div className={styles.item}>
          <SpecialistNavBar />
        </div>
        <div className={styles.item}>
          <LangSelect />
        </div>

        <div
          className={styles.itembutton}
          onClick={() => navigate("/login", { replace: false })}
        >
          {t("log_in")}
        </div>
      </div>
    </div>
  );
};

export default GuestTopBar;

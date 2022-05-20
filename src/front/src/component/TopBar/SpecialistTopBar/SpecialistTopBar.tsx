import { useNavigate } from "react-router-dom";
import styles from "./specialistTopBar.module.scss";
import { Logout } from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import { useTranslation } from "react-i18next";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";

const SpecialistTopBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  return (
    <div className={styles.topBar}>
      <div className={styles.logo} onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className={styles.links}>
        <div className={styles.item}>{t("cyber")}</div>
        <div className={styles.item}>
          <SpecialistNavBar />
        </div>
        <div
          className={styles.item}
          onClick={() => navigate("/specialist", { replace: false })}
        >
          SPECIALISTPAGE
        </div>
        <div className={styles.item}>
          <LangSelect />
        </div>
        <Logout />
      </div>
    </div>
  );
};

export default SpecialistTopBar;

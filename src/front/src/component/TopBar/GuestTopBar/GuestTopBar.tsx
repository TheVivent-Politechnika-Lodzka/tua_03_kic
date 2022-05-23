import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import styles from "./guestTopBar.module.scss";
import Logo from "../../Logo/Logo";

const GuestTopBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  return (
    <div className={styles.topBar}>
      <Logo/>
      <div className={styles.links}>
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

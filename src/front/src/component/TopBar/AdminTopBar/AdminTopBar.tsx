import { useDispatch } from "react-redux";
import { useNavigate } from "react-router-dom";
import { logout } from "../../../redux/userSlice";
import styles from "./adminTopBar.module.scss";
import {Logout} from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import { useTranslation } from "react-i18next";

const AdminTopBar = () => {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const {t} = useTranslation();
  return (
    <div className={styles.topBar}>
      <div className={styles.logo} onClick={() => navigate("/", { replace: false })}>
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
      <div className={styles.links}>
        <div className={styles.item}>{t("cyber")}</div>
        <div className={styles.item}>{t("specialist")}</div>
        <div
          className={styles.item}
          onClick={() => navigate("/admin", { replace: false })}
        >
          ADMINPAGE
        </div>
        <div className={styles.item}>
          <LangSelect />
        </div>
          <Logout/>
      </div>
    </div>
  );
};

export default AdminTopBar;

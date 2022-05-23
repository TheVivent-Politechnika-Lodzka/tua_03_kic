import { useNavigate } from "react-router-dom";
import styles from "./clientTopBar.module.scss";
import { Logout } from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import { useTranslation } from "react-i18next";
import ImplantNavBar from "../../NavBar/ImplantNavBar/ImplantNavBar";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";
import AppointmentNavBar from "../../NavBar/AppointmentNavBar/AppointmentNavBar";
import AccessLevelSelect from "../../Selector/AccessLevelSelect/AccessLevelSelect";
import Logo from "../../Logo/Logo";
import UserNavBar from "../../NavBar/UserNavBar/UserNavBar";

const ClientTopBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  return (
    <div className={styles.topBar}>
      <Logo />
      <div className={styles.links}>
        <div className={styles.item}>
          <UserNavBar />
        </div>

        <div className={styles.item}>
          <LangSelect />
        </div>
        <div className={styles.item}>
          <AccessLevelSelect />
        </div>
        <Logout />
      </div>
    </div>
  );
};

export default ClientTopBar;

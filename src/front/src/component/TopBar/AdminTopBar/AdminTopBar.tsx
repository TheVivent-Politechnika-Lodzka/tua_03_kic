import styles from "./adminTopBar.module.scss";
import { Logout } from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import AppointmentNavBar from "../../NavBar/AppointmentNavBar/AppointmentNavBar";
import ImplantNavBar from "../../NavBar/ImplantNavBar/ImplantNavBar";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";
import AccessLevelSelect from "../../Selector/AccessLevelSelect/AccessLevelSelect";
import Logo from "../../Logo/Logo";

const AdminTopBar = () => {
  return (
    <div className={styles.topBar}>
      <Logo />
      <div className={styles.links}>
        <div className={styles.item}>
          <AppointmentNavBar />
        </div>
        <div className={styles.item}>
          <ImplantNavBar />
        </div>
        <div className={styles.item}>
          <SpecialistNavBar />
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

export default AdminTopBar;

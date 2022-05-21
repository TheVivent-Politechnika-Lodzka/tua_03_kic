import styles from "./specialistTopBar.module.scss";
import { Logout } from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";
import AppointmentNavBar from "../../NavBar/AppointmentNavBar/appointmentNavBar";
import ImplantNavBar from "../../NavBar/ImplantNavBar/ImplantNavBar";
import AccessLevelSelect from "../../Selector/AccessLevelSelect/AccessLevelSelect";
import Logo from "../../Logo/Logo";

const SpecialistTopBar = () => {
  
  return (
    <div className={styles.topBar}>
      <Logo/>
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
          <AccessLevelSelect />
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

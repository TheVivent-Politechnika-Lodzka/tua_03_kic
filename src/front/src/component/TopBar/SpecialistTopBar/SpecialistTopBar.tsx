import styles from "./specialistTopBar.module.scss";
import { Logout } from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import SpecialistNavBar from "../../NavBar/SpecialistNavBar/SpecialistNavBar";
import ImplantNavBar from "../../NavBar/ImplantNavBar/ImplantNavBar";
import Logo from "../../Logo/Logo";
import AppointmentNavBar from "../../NavBar/AppointmentNavBar/AppointmentNavBar";
import AccessLevelSelect from "../../Selector/AccessLevelSelect/AccessLevelSelect";
import UserNavBar from "../../NavBar/UserNavBar/UserNavBar";

const SpecialistTopBar = () => {
  return (
    <div className={styles.topBar}>
      <Logo />
      <div className={styles.links}>
       
        <div className={styles.item}>
          <SpecialistNavBar />
        </div>
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

export default SpecialistTopBar;

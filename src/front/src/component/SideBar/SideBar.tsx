import styles from "./sidebar.module.scss";
import SpecialistNavBar from "../NavBar/SpecialistNavBar/SpecialistNavBar";
import Logo from "../Logo/Logo";
import AppointmentNavBar from "../NavBar/AppointmentNavBar/AppointmentNavBar";
import ImplantNavBar from "../NavBar/ImplantNavBar/ImplantNavBar";

const SideBar = () => {
  return (
    <div className={styles.content}>
      <Logo/>
      <div className={styles.item}>
        Mój profil
      </div>
      <div className={styles.item2}>
        <SpecialistNavBar />
      </div>
      <div className={styles.item2}>
        <AppointmentNavBar />
      </div>
      <div className={styles.item2}>
        <ImplantNavBar />
      </div>
    </div>
  );
};

export default SideBar;

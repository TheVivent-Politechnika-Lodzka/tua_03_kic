import styles from "./sidebar.module.scss";
import SpecialistNavBar from "../NavBar/SpecialistNavBar/SpecialistNavBar";
import AppointmentNavBar from "../NavBar/AppointmentNavBar/appointmentNavBar";
import ImplantNavBar from "../NavBar/ImplantNavBar/ImplantNavBar";
import Logo from "../Logo/Logo";

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

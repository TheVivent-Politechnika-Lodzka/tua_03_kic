import styles from "./sidebar.module.scss";
import SpecialistNavBar from "../NavBar/SpecialistNavBar/SpecialistNavBar";
import Logo from "../Logo/Logo";
import AppointmentNavBar from "../NavBar/AppointmentNavBar/AppointmentNavBar";
import ImplantNavBar from "../NavBar/ImplantNavBar/ImplantNavBar";
import UserNavBar from "../NavBar/UsersNavBar/SpecialistNavBar";

const SideBar = () => {
  return (
    <div className={styles.sideBar}>
      <Logo />
      <div className={styles.myProfile}>Mój profil</div>
      <div className={styles.itemsWrapper}>
        <div className={styles.item}>
          <UserNavBar />
        </div>
        <div className={styles.item}>
          <SpecialistNavBar />
        </div>
        <div className={styles.item}>
          <AppointmentNavBar />
        </div>
        <div className={styles.item}>
          <ImplantNavBar />
        </div>
      </div>
    </div>
  );
};

export default SideBar;

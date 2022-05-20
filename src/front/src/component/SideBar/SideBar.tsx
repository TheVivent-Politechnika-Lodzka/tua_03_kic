import styles from "./sidebar.module.scss";
import SpecialistNavBar from "../NavBar/SpecialistNavBar/SpecialistNavBar";
import { useNavigate } from "react-router";
import AppointmentNavBar from "../NavBar/AppointmentNavBar/appointmentNavBar";
import ImplantNavBar from "../NavBar/ImplantNavBar/ImplantNavBar";

const SideBar = () => {
  const navigate = useNavigate();

  return (
    <div className={styles.content}>
      <div
        className={styles.logo}
        onClick={() => navigate("/", { replace: false })}
      >
        <img src="logo.jpg" alt="Logo" height="80px" />
      </div>
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

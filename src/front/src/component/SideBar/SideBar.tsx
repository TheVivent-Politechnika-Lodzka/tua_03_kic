import styles from "./sidebar.module.scss";
import SpecialistNavBar from "../NavBar/SpecialistNavBar/SpecialistNavBar";
import Logo from "../Logo/Logo";
import UserNavBar from "../NavBar/UserNavBar/UserNavBar";
import { useStoreSelector } from "../../redux/reduxHooks";

const SideBar = () => {
  const user = useStoreSelector((state) => state.user.cur);
  return (
    <div className={styles.sideBar}>
      <Logo />
      <div className={styles.myProfile}>Mój profil</div>
      <div className={styles.itemsWrapper}>
        <div className={styles.item}>
          <UserNavBar />
        </div>
      </div>
    </div>
  );
};

export default SideBar;

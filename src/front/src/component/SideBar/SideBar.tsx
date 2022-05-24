import styles from "./sidebar.module.scss";
import Logo from "../Logo/Logo";
import UserNavBar from "../NavBar/UserNavBar/UserNavBar";

const SideBar = () => {
  return (
    <div className={styles.sideBar}>
      <Logo />
      <div className={styles.itemsWrapper}>
        <div className={styles.item}>
          <UserNavBar />
        </div>
      </div>
    </div>
  );
};

export default SideBar;

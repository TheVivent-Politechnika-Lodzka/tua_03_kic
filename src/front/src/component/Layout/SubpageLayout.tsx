import { Outlet } from "react-router";
import SideBar from "../SideBar/SideBar";
import SubpageTopBar from "../TopBar/SubpageTopBar/SubpageTopBar";
import styles from "./detailsLayout.module.scss";

const SubpageLayout: React.FC<{}> = () => {
  return (
    <div>
      <SubpageTopBar />

      <div className={styles.content}>
        <div className={styles.left_side}>
          <SideBar />
        </div>
        <div className={styles.right_side}>
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default SubpageLayout;

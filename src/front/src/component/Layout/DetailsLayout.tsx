import { Outlet } from "react-router";
import SideBar from "../SideBar/SideBar";
import DetailsTopBar from "../TopBar/DetailsTopBar/DetailsTopBar";
import styles from "./detailsLayout.module.scss";

const DetailsLayout = () => {
  return (
    <div className={styles.content}>
      
      <DetailsTopBar/>
      <div className={styles.left_side}>
        <SideBar />
      </div>
      <div className={styles.right_side}>
        <Outlet />
      </div>
    </div>
  );
};

export default DetailsLayout;

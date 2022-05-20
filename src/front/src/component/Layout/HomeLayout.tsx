import { Link } from "react-router-dom";
import SideBar from "../SideBar/SideBar";
import SelectorBar from "../TopBar/SelectorTopBar/SelectorTopBar";
import { Outlet } from "react-router";

const HomeLayout: React.FC<{}> = () => {
  return (
    <>
      <SelectorBar />
      <>
        <Outlet />
      </>
    </>
  );
};

export default HomeLayout;

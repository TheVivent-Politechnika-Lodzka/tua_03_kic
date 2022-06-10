import jwtDecode from "jwt-decode";
import { useEffect } from "react";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import { JWT } from "../../../api/types/common";
import Navbar from "../../../component/Navbar/Navbar";
import Sidebar from "../../../component/Sidebar/Sidebar";
import { useStoreDispatch } from "../../../redux/reduxHooks";
import { logout } from "../../../redux/userSlice";
import "./style.scss";

const AuthorizedLayoutPage = () => {
    const location = useLocation();
    const dispatch = useStoreDispatch();

    //TODO: Ogarnąć refresh tokena
    useEffect(() => {
        const token = localStorage.getItem("ACCESS_TOKEN");
        const result: JWT = jwtDecode(token as string);
        const date = Math.floor(new Date().getTime() / 1000);

        if (result.exp < date) {
            localStorage.removeItem("ACCESS_TOKEN");
            dispatch(logout());
        }
    }, [location.pathname]);

    return (
        <div className="authorized-layout-page">
            <div className="sidebar-wrapper">
                <Sidebar />
            </div>
            <div className="content-wrapper">
                <Navbar />
                <Outlet />
            </div>
        </div>
    );
};

export default AuthorizedLayoutPage;

import { faSignOut } from "@fortawesome/free-solid-svg-icons";
import SideBarRoute from "../SidebarRoute/SidebarRoute";
import KICLogo from "../../assets/logo.svg";
import "./style.scss";
import { useEffect, useState } from "react";
import { handleSwitchSidebarItems as items } from "../../utils/sidebarItems";
import { logout } from "../../redux/userSlice";
import { useTranslation } from "react-i18next";
import { useStoreDispatch, useStoreSelector } from "../../redux/reduxHooks";
import { useLocation, useNavigate } from "react-router";

const Sidebar = () => {
    const [expanded, setExpanded] = useState<boolean>(false);
    const [currentRoute, setCurrentRoute] = useState<number>(0);

    const accessLevel = useStoreSelector((state) => state.user.cur);
    const { t } = useTranslation();
    const dispatch = useStoreDispatch();
    const navigate = useNavigate();

    const handleChange = (index: number) => {
        if (index === currentRoute) {
            return;
        }
        setCurrentRoute(index);
    };

    const handleLogout = () => {
        localStorage.removeItem("ACCESS_TOKEN");
        dispatch(logout());
        navigate("/");
        window.location.reload();
    };
    const location = useLocation();

    useEffect(() => {
        items(accessLevel as AccessLevelType).forEach((item, index) => {
            if (item.path === location.pathname) {
                setCurrentRoute(index);
                return;
            }
        });
    }, []);

    useEffect(() => {}, [accessLevel]);
    return (
        <div className={`sidebar ${expanded && `expanded`}`}>
            <div
                onClick={() => {
                    setExpanded(!expanded);
                }}
                className="logo-wrapper"
            >
                <img src={KICLogo} alt="KIC logo" className="logo" />
                {expanded && <p className="logo-name">KIC</p>}
            </div>
            <div className="routes-wrapper">
                {items(accessLevel as AccessLevelType).map(
                    ({ title, icon, path }, index) => {
                        return (
                            <SideBarRoute
                                active={index === currentRoute}
                                key={title}
                                expanded={expanded}
                                title={title}
                                icon={icon}
                                onClick={() => {
                                    handleChange(index);
                                    navigate(path);
                                }}
                            />
                        );
                    }
                )}
            </div>
            {accessLevel && (
                <div className="logout-wrapper">
                    <SideBarRoute
                        expanded={expanded}
                        title={"Wyloguj się"}
                        icon={faSignOut}
                        onClick={handleLogout}
                    />
                </div>
            )}
        </div>
    );
};

export default Sidebar;

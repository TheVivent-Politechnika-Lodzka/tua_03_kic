import avatar from "../../assets/images/avatar.jpg";
import { useStoreSelector } from "../../redux/reduxHooks";
import UserAccessLevel from "./UserAccessLevel";
import "./style.scss";
import { useNavigate } from "react-router";

const Navbar = () => {
    const login = useStoreSelector((state) => state.user.sub);
    const accessLevel = useStoreSelector((state) => state.user.cur);
    const navigate = useNavigate();

    return (
        <nav className={`navbar ${accessLevel.toLowerCase()}`}>
            {accessLevel ? (
                <div className="user-info-wrapper">
                    <div className="user-text-wrapper">
                        <p className="user-login">{login}</p>
                        <UserAccessLevel accessLevel={accessLevel} />
                    </div>
                    <img
                        src={avatar}
                        alt="Avatar użytkownika"
                        className="user-avatar"
                    />
                </div>
            ) : (
                <div className="navbar-button-wrapper">
                    <div
                        onClick={() => {
                            navigate("/login");
                        }}
                        className="navbar-button-login"
                    >
                        Zaloguj
                    </div>
                    <div
                        onClick={() => {
                            navigate("/login");
                        }}
                        className="navbar-button-register"
                    >
                        Zarejestruj się
                    </div>
                </div>
            )}
        </nav>
    );
};

export default Navbar;

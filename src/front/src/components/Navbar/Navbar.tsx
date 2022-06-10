import avatar from "../../assets/images/avatar.jpg";
import { useStoreSelector } from "../../redux/reduxHooks";
import UserAccessLevel from "./UserAccessLevel";
import "./style.scss";

const Navbar = () => {
    const login = useStoreSelector((state) => state.user.sub);
    const accessLevel = useStoreSelector((state) => state.user.cur);

    return (
        <nav className={`navbar ${accessLevel.toLowerCase()}`}>
            {/* <Select className="language-select" data={languageSelect} /> */}
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
        </nav>
    );
};

export default Navbar;

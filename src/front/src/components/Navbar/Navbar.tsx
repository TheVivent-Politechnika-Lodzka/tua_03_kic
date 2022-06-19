import avatar from "../../assets/images/avatar.jpg";
import { useStoreSelector } from "../../redux/reduxHooks";
import UserAccessLevel from "./UserAccessLevel";
import { useNavigate } from "react-router";
import styles from "./style.module.scss";

const Navbar = () => {
    const login = useStoreSelector((state) => state.user.sub);
    const accessLevel = useStoreSelector((state) => state.user.cur);
    const navigate = useNavigate();

    return (
        <nav
            className={`${styles.navbar} ${styles[accessLevel.toLowerCase()]}`}
        >
            {accessLevel ? (
                <div className={styles.user_info_wrapper}>
                    <div className={styles.user_text_wrapper}>
                        <p className={styles.login}>{login}</p>
                        <UserAccessLevel accessLevel={accessLevel} />
                    </div>
                    <img
                        src={avatar}
                        alt="Avatar użytkownika"
                        className={styles.avatar}
                    />
                </div>
            ) : (
                <div className={styles.navbar_button_wrapper}>
                    <div
                        onClick={() => {
                            navigate("/login");
                        }}
                        className={styles.login}
                    >
                        Zaloguj
                    </div>
                    <div
                        onClick={() => {
                            navigate("/register");
                        }}
                        className={styles.register}
                    >
                        Zarejestruj się
                    </div>
                </div>
            )}
        </nav>
    );
};

export default Navbar;

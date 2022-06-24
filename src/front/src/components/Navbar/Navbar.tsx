import avatar from "../../assets/images/avatar.jpg";
import { useStoreSelector } from "../../redux/reduxHooks";
import UserAccessLevel from "./UserAccessLevel";
import { useNavigate } from "react-router";
import styles from "./style.module.scss";
import Clocker from "./Clocker";
import { useTranslation } from "react-i18next";
import { getOwnAccount } from "../../api";
import { useEffect, useState } from "react";

const Navbar = () => {
    const login = useStoreSelector((state) => state.user.sub);
    const accessLevel = useStoreSelector((state) => state.user.cur);
    const navigate = useNavigate();
    const { t } = useTranslation();
    const [account, setAccount] = useState<AccountDetails>();

    const handleGetOwnAccount = async () => {
        try {
            const data = await getOwnAccount();
            setAccount(data as AccountDetails);
        } catch (error: ApiError | any) {
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    useEffect(() => {
        handleGetOwnAccount();
    }, []);

    return (
        <nav
            className={`${styles.navbar} ${styles[accessLevel.toLowerCase()]}`}
        >
            {accessLevel ? (
                <div className={styles.user_info_wrapper}>
                    <div className={styles.time_wrapper}>
                        <Clocker />
                    </div>

                    <div className={styles.user_text_wrapper}>
                        <p className={styles.login}>{login}</p>
                        <UserAccessLevel accessLevel={accessLevel} />
                    </div>
                    <img
                        src={account?.url}
                        alt={t("navbar.avatar_alt")}
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
                        {t("navbar.log")}
                    </div>
                    <div
                        onClick={() => {
                            navigate("/register");
                        }}
                        className={styles.register}
                    >
                        {t("navbar.register")}
                    </div>
                </div>
            )}
        </nav>
    );
};

export default Navbar;

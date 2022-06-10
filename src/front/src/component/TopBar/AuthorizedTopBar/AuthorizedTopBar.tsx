import styles from "./authorizedTopBar.module.scss";
import { Logout } from "../SelectorTopBar/SelectorTopBar";
import LangSelect from "../../Selector/LangBarSelect/LangSelect";
import AccessLevelSelect from "../../Selector/AccessLevelSelect/AccessLevelSelect";
import Logo from "../../Logo/Logo";

const AuthorizedTopBar = () => {
    return (
        <div className={styles.topBar}>
            <Logo />
            <div className={styles.links}>
                <div className={styles.item}>
                    <p>test</p>
                </div>
                <div className={styles.item}>
                    <LangSelect />
                </div>
                <div className={styles.item}>
                    <AccessLevelSelect />
                </div>
                <Logout />
            </div>
        </div>
    );
};

export default AuthorizedTopBar;

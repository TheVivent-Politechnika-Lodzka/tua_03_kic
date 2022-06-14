import {
    faUser,
    faUserCog,
    faUserDoctor,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import styles from "./style.module.scss";

interface UserAccessLevelProps {
    accessLevel: string;
}

const UserAccessLevel = ({ accessLevel }: UserAccessLevelProps) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return (
                <div className={styles.user_access_level_wrapper}>
                    <FontAwesomeIcon className={styles.icon} icon={faUserCog} />
                    <p className={styles.text}>Administrator</p>
                </div>
            );
        }
        case "SPECIALIST": {
            return (
                <div className={styles.user_access_level_wrapper}>
                    <FontAwesomeIcon
                        className={styles.icon}
                        icon={faUserDoctor}
                    />
                    <p className={styles.text}>Specjalista</p>
                </div>
            );
        }
        case "CLIENT": {
            return (
                <div className={styles.user_access_level_wrapper}>
                    <FontAwesomeIcon className={styles.icon} icon={faUser} />
                    <p className={styles.text}>Klient</p>
                </div>
            );
        }
        default: {
            return <></>;
        }
    }
};

export default UserAccessLevel;

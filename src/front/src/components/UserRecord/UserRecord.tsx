import { faCheck, faInfo, faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { style } from "@mui/system";
import { AccessLevelDto } from "../../api/types/apiParams";
import AccessLevel from "../shared/AccessLevel/AccessLevel";
import styles from "./style.module.scss";

const UserRecord = () => {
    return (
        <div className={styles.user_record_wrapper}>
            <div className={styles.avatar_wrapper}>
                <img
                    className={styles.avatar}
                    src="https://i.pravatar.cc/100"
                    alt="user"
                />
                <div className={styles.access_levels_wrapper}>
                    <AccessLevel accessLevel="SPECIALIST" />
                    <AccessLevel accessLevel="ADMINISTRATOR" />
                </div>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>JohnKcoc123</p>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>Jan</p>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>Paweł</p>
            </div>
            <div className={styles.detail_wrapper}>
                <FontAwesomeIcon className={styles.icon} icon={faCheck} />
                <p className={styles.detail}>Zatwierdzone</p>
            </div>
            <div className={styles.detail_wrapper}>
                <div
                    className={`${styles.is_active_circle} ${
                        true ? styles.active : styles.inactive
                    } `}
                />
                <p className={styles.detail}>Aktywny</p>
            </div>
            <div className={styles.detail_wrapper}>
                <div className={styles.info_button} onClick={() => {}}>
                    <FontAwesomeIcon icon={faInfoCircle} />
                </div>
            </div>
        </div>
    );
};

export default UserRecord;

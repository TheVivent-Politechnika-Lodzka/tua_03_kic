import {
    faC,
    faCancel,
    faCheck,
    faInfoCircle,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useState } from "react";
import { useNavigate } from "react-router";
import AccountDetails from "../AccountDetails/AccountDetails";
import AccessLevel from "../shared/AccessLevel/AccessLevel";
import styles from "./style.module.scss";

interface UserRecordProps {
    user: AccountDetails;
}

const UserRecord = ({ user }: UserRecordProps) => {
    const [modal, setModal] = useState<boolean>(false);
    const navigate = useNavigate();

    return (
        <div className={styles.user_record_wrapper}>
            <div className={styles.avatar_wrapper}>
                <img
                    className={styles.avatar}
                    src="https://i.pravatar.cc/100"
                    alt="user"
                />
                <div className={styles.access_levels_wrapper}>
                    {user?.accessLevels.map((accessLevel, index) => (
                        <AccessLevel
                            key={user?.login + index}
                            accessLevel={accessLevel?.level}
                        />
                    ))}
                </div>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>{user?.login}</p>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>{user?.firstName}</p>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>{user?.lastName}</p>
            </div>
            <div className={styles.detail_wrapper}>
                <FontAwesomeIcon
                    className={styles.icon}
                    icon={user?.confirmed ? faCheck : faCancel}
                />
                <p className={styles.detail}>
                    {user?.confirmed ? "Zatwierdzone" : "Niezatwierdzone"}
                </p>
            </div>
            <div className={styles.detail_wrapper}>
                <div
                    className={`${styles.is_active_circle} ${
                        user?.active ? styles.active : styles.inactive
                    } `}
                />
                <p className={styles.detail}>
                    {user?.active ? "Aktywne" : "Nieaktywne"}
                </p>
            </div>
            <div className={styles.detail_wrapper}>
                <div
                    className={styles.info_button}
                    onClick={() => {
                        setModal(true);
                    }}
                >
                    <FontAwesomeIcon icon={faInfoCircle} />
                </div>
            </div>
            <AccountDetails login={user?.login} isOpened={modal} onClose={() => {setModal(false)}} />
        </div>
    );
};

export default UserRecord;

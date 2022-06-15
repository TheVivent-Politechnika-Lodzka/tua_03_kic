import {
    faClose,
    faEdit,
    faKey,
    faLock,
    faUnlockAlt,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import ReactModal from "react-modal";
import ReactLoading from "react-loading";
import avatar from "../../assets/images/avatar.jpg";
import AccessLevel from "../shared/AccessLevel/AccessLevel";
import flagPL from "../../assets/images/PL.png";
import flagEN from "../../assets/images/EN.png";
import { getAccount } from "../../api";
import ActionButton from "../shared/ActionButton/ActionButton";
import { useNavigate } from "react-router";
import styles from "./style.module.scss";

interface AccountDetailsProps {
    login: string;
    isOpened: boolean;
    onClose: () => void;
}

const AccountDetails = ({ login, isOpened, onClose }: AccountDetailsProps) => {
    const [account, setAccount] = useState<AccountDetails>();
    const [loading, setLoading] = useState<Loading>({ pageLoading: true });

    const navigate = useNavigate();

    const handleGetAccount = async () => {
        try {
            setLoading({ pageLoading: true });
            const data = await getAccount(login);
            console.log(data);
            setAccount(data as AccountDetails);
            setLoading({ pageLoading: false });
        } catch (error: ApiError | any) {
            setLoading({ pageLoading: false });
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    const customStyles = {
        overlay: {
            backgroundColor: "rgba(0, 0, 0, 0.85)",
        },
        content: {
            width: "100vw",
            height: "100vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: "transparent",
            border: "none",
        },
    };

    useEffect(() => {
        handleGetAccount();
    }, [login]);

    return (
        <ReactModal isOpen={isOpened} style={customStyles} ariaHideApp={false}>
            <section className={styles.accountDetailsPage}>
                {loading.pageLoading ? (
                    <ReactLoading
                        type="cylon"
                        color="#fff"
                        width="10rem"
                        height="10rem"
                    />
                ) : (
                    <div className={styles.accountDetailsContent}>
                        <FontAwesomeIcon
                            className={styles.closeIcon}
                            icon={faClose}
                            onClick={onClose}
                        />
                        <div className={styles.accountGeneralDetails}>
                            <img
                                className={styles.accountAvatar}
                                src={avatar}
                                alt="Account general: Avatar"
                            />
                            <p className={styles.accountLogin}>
                                {account?.login}
                            </p>
                            <div className={styles.accountAccessLevelsWrapper}>
                                <p className={styles.accountAccessLevelsTitle}>
                                    Poziomy dostępu:
                                </p>
                                {account?.accessLevels.map((accessLevel) => (
                                    <AccessLevel
                                        key={accessLevel.level}
                                        accessLevel={accessLevel.level}
                                    />
                                ))}
                            </div>
                            <div className={styles.accountActionsWrapper}>
                                <ActionButton
                                    onClick={() => {
                                        navigate(`/accounts/${login}`);
                                    }}
                                    title="Edytuj konto"
                                    color="green"
                                    icon={faEdit}
                                />
                                <ActionButton
                                    onClick={() => {
                                        navigate(
                                            `/accounts/${login}/change-password`
                                        );
                                    }}
                                    title="Zmień hasło"
                                    color="purple"
                                    icon={faKey}
                                />
                                <ActionButton
                                    title={
                                        account?.isActive
                                            ? "Zablokuj konto"
                                            : "Odblokuj konto"
                                    }
                                    color="orange"
                                    icon={
                                        account?.isActive ? faUnlockAlt : faLock
                                    }
                                />
                            </div>
                        </div>
                        <div className={styles.accountDetails}>
                            <p className={styles.detailsTitle}>
                                Dane szczegółowe
                            </p>
                            <div className={styles.detailsWrapper}>
                                <div className={styles.detailWrapper}>
                                    <p className={styles.detailTitle}>Imię:</p>
                                    <p className={styles.detailDescription}>
                                        {account?.firstName}
                                    </p>
                                </div>
                                <div className={styles.detailWrapper}>
                                    <p className={styles.detailTitle}>
                                        Nazwisko:
                                    </p>
                                    <p className={styles.detailDescription}>
                                        {account?.lastName}
                                    </p>
                                </div>
                                <div className={styles.detailWrapper}>
                                    <p className={styles.detailTitle}>Email:</p>
                                    <p className={styles.detailDescription}>
                                        {account?.email}
                                    </p>
                                </div>
                                {account?.accessLevels.some((level) => {
                                    return level.level === "CLIENT";
                                }) && (
                                    <div className={styles.detailWrapper}>
                                        <p className={styles.detailTitle}>
                                            Numer PESEL:
                                        </p>
                                        <p className={styles.detailDescription}>
                                            {account?.accessLevels
                                                .filter(
                                                    (level) =>
                                                        level.level === "CLIENT"
                                                )
                                                .map((level) => level.pesel)}
                                        </p>
                                    </div>
                                )}
                                {account?.accessLevels.some((level) => {
                                    return (
                                        level.level === "ADMINISTRATOR" ||
                                        level.level === "SPECIALIST"
                                    );
                                }) && (
                                    <div className={styles.detailWrapper}>
                                        <p className={styles.detailTitle}>
                                            Numer telefonu:
                                        </p>
                                        <p className={styles.detailDescription}>
                                            {account?.accessLevels
                                                .filter(
                                                    (level) =>
                                                        level.level ===
                                                            "ADMINISTRATOR" ||
                                                        "SPECIALIST"
                                                )
                                                .map(
                                                    (level) => level.phoneNumber
                                                )}
                                        </p>
                                    </div>
                                )}
                                <div className={styles.detailWrapper}>
                                    <p className={styles.detailTitle}>Język:</p>
                                    <img
                                        src={
                                            account?.language?.language === "pl"
                                                ? flagPL
                                                : flagEN
                                        }
                                        alt="Detail description: language"
                                        className={styles.detailFlag}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                )}
            </section>
        </ReactModal>
    );
};

export default AccountDetails;

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
import {
    activateAccount,
    deactivateAccount,
    getAccount,
    GetAccountResponse,
} from "../../api";
import ActionButton from "../shared/ActionButton/ActionButton";
import { useNavigate } from "react-router";
import styles from "./style.module.scss";
import ConfirmActionModal from "../ConfirmActionModal/ConfirmActionModal";
import { showNotification } from "@mantine/notifications";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../utils/showNotificationsItems";
import Modal from "../Modal/Modal";

interface AccountDetailsProps {
    login: string;
    isOpened: boolean;
    onClose: () => void;
}

const AccountDetails = ({ login, isOpened, onClose }: AccountDetailsProps) => {
    const [account, setAccount] = useState<GetAccountResponse>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [opened, setOpened] = useState<boolean>(false);

    const navigate = useNavigate();

    const handleGetAccount = async () => {
        try {
            setLoading({ pageLoading: true });
            const data = await getAccount(login);
            setAccount(data as GetAccountResponse);
            setLoading({ pageLoading: false });
        } catch (error: ApiError | any) {
            setLoading({ pageLoading: false });
            alert(`${error?.status} ${error?.errorMessage}`);
        }
    };

    const handleDeactivateActivateAccount = async (deactivate: boolean) => {
        try {
            setLoading({ ...loading, actionLoading: true });
            if (deactivate) {
                await deactivateAccount(
                    account?.login as string,
                    account?.etag as string
                );
                setLoading({ ...loading, actionLoading: false });
                showNotification(
                    successNotficiationItems("Konto zostało deaktywowane")
                );
                onClose();
                return;
            }

            await activateAccount(
                account?.login as string,
                account?.etag as string
            );
            setLoading({ ...loading, actionLoading: false });
            showNotification(
                successNotficiationItems("Konto zostało aktywowane")
            );
            onClose();
        } catch (error: ApiError | any) {
            setLoading({ pageLoading: false });
            showNotification(failureNotificationItems(error?.errorMessage));
        }
    };


    useEffect(() => {
        handleGetAccount();
    }, [isOpened]);

    return (
        <Modal isOpen={isOpened}>
            <section className={styles.account_details_page}>
                {loading.pageLoading ? (
                    <ReactLoading
                        type="bars"
                        color="#fff"
                        width="10rem"
                        height="10rem"
                    />
                ) : (
                    <div className={styles.content}>
                        <FontAwesomeIcon
                            className={styles.close_icon}
                            icon={faClose}
                            onClick={onClose}
                        />
                        <div className={styles.account_general_details}>
                            <img
                                className={styles.avatar}
                                src={avatar}
                                alt="Account general: Avatar"
                            />
                            <p className={styles.login}>{account?.login}</p>
                            <div className={styles.access_levels_wrapper}>
                                <p className={styles.title}>Poziomy dostępu:</p>
                                {account?.accessLevels.map((accessLevel) => (
                                    <AccessLevel
                                        key={accessLevel.level}
                                        accessLevel={accessLevel.level}
                                    />
                                ))}
                            </div>
                            <div className={styles.actions_wrapper}>
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
                                        account?.active
                                            ? "Zablokuj konto"
                                            : "Odblokuj konto"
                                    }
                                    color="orange"
                                    icon={
                                        account?.active ? faLock : faUnlockAlt
                                    }
                                    onClick={() => {
                                        setOpened(true);
                                    }}
                                />
                            </div>
                        </div>
                        <div className={styles.account_details}>
                            <p className={styles.account_details_title}>
                                Dane szczegółowe
                            </p>
                            <div className={styles.details_wrapper}>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Imię:</p>
                                    <p className={styles.description}>
                                        {account?.firstName}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Nazwisko:</p>
                                    <p className={styles.description}>
                                        {account?.lastName}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Email:</p>
                                    <p className={styles.description}>
                                        {account?.email}
                                    </p>
                                </div>
                                {account?.accessLevels.some((level) => {
                                    return level.level === "CLIENT";
                                }) && (
                                    <div className={styles.detail_wrapper}>
                                        <p className={styles.title}>
                                            Numer PESEL:
                                        </p>
                                        <p className={styles.description}>
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
                                    <div className={styles.detail_wrapper}>
                                        <p className={styles.title}>
                                            Numer telefonu:
                                        </p>
                                        <p className={styles.description}>
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
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Język:</p>
                                    <img
                                        src={
                                            account?.language?.language === "pl"
                                                ? flagPL
                                                : flagEN
                                        }
                                        alt="Detail description: language"
                                        className={styles.flag}
                                    />
                                </div>
                            </div>
                        </div>
                        <ConfirmActionModal
                            title={
                                account?.active
                                    ? "Zablokuj konto"
                                    : "Odblokuj konto"
                            }
                            description={`Czy na pewno chcesz ${
                                account?.active ? "zablokować" : "odblokować"
                            } konto? Operacja jest nieodwracalna.`}
                            isLoading={loading.actionLoading as boolean}
                            isOpened={opened}
                            handleFunction={async () => {
                                await handleDeactivateActivateAccount(
                                    account?.active as boolean
                                );
                                setOpened(false);
                            }}
                            onClose={() => {
                                setOpened(false);
                            }}
                        />
                    </div>
                )}
            </section>
        </Modal>
    );
};

export default AccountDetails;

import {
    faClose,
    faEdit,
    faKey,
    faLock,
    faUnlockAlt,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import ReactLoading from "react-loading";
import avatar from "../../../../assets/images/avatar.jpg";
import AccessLevel from "../../../../components/shared/AccessLevel/AccessLevel";
import flagPL from "../../../../assets/images/PL.png";
import flagEN from "../../../../assets/images/EN.png";
import {
    activateAccount,
    deactivateAccount,
    getAccount,
    GetAccountResponse,
} from "../../../../api";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import { useNavigate } from "react-router";
import styles from "./style.module.scss";
import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";
import { showNotification } from "@mantine/notifications";
import { successNotficiationItems } from "../../../../utils/showNotificationsItems";
import Modal from "../../../../components/shared/Modal/Modal";
import ChangeUserPasswordPage from "../ChangeUserPasswordPage/ChangeUserPasswordPage";

interface AccountDetailsPageProps {
    login: string;
    isOpened: boolean;
    onClose: () => void;
    isAdmin: boolean;
}

const AccountDetailsPage = ({ login, isOpened, isAdmin, onClose }: AccountDetailsPageProps) => {
    const [account, setAccount] = useState<GetAccountResponse>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [changePasswordModal, setChangePasswordModal] =
        useState<boolean>(false);
    const [confirmActionModal, setConfirmActionModal] =
        useState<boolean>(false);

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
        setLoading({ ...loading, actionLoading: true });

        // let response;
        if (deactivate) {
            await deactivateAccount(
                account?.login as string,
                account?.etag as string
            );
        } else {
            await activateAccount(
                account?.login as string,
                account?.etag as string
            );
        }
        // TODO: Ogarnąć dlaczego wywala wyjątek. Ale i tak bez tego działa elegancko
        // if ("errorMessage" in response) {
        //     setLoading({ ...loading, actionLoading: false });
        //     showNotification(failureNotificationItems(response.errorMessage));
        //     setConfirmActionModal(false);
        //     onClose();
        //     return;
        // }
        setLoading({ ...loading, actionLoading: false });
        showNotification(
            successNotficiationItems(
                `Konto zostało ${deactivate ? "deaktywowane" : "aktywowane"}`
            )
        );
        setConfirmActionModal(false);
        onClose();
        return;
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
                                    isDisabled={isAdmin}
                                    onClick={() => {
                                        navigate(`/accounts/${login}`);
                                    }}
                                    title="Edytuj konto"
                                    color="green"
                                    icon={faEdit}
                                />
                                <ActionButton
                                    isDisabled={isAdmin}
                                    onClick={() => {
                                        setChangePasswordModal(true);
                                    }}
                                    title="Zmień hasło"
                                    color="purple"
                                    icon={faKey}
                                />
                                <ActionButton
                                    isDisabled={isAdmin}
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
                                        setConfirmActionModal(true);
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
                        <ChangeUserPasswordPage
                            isOpen={changePasswordModal}
                            onClose={() => {
                                setChangePasswordModal(false);
                            }}
                            login={login}
                        />
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
                            isOpened={confirmActionModal}
                            handleFunction={async () => {
                                await handleDeactivateActivateAccount(
                                    account?.active as boolean
                                );
                            }}
                            onClose={() => {
                                setConfirmActionModal(false);
                            }}
                        />
                    </div>
                )}
            </section>
        </Modal>
    );
};

export default AccountDetailsPage;
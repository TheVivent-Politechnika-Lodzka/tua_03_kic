import {
    faClose,
    faEdit,
    faKey,
    faLock,
    faUnlockAlt,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useContext, useEffect, useState } from "react";
import ReactModal from "react-modal";
import ReactLoading from "react-loading";
import avatar from "../../assets/images/avatar.jpg";
import AccessLevel from "../shared/AccessLevel/AccessLevel";
import flagPL from "../../assets/images/PL.png";
import flagEN from "../../assets/images/EN.png";
import {
    activateAccount,
    addAccesLevel,
    AddAccessLevelResponse,
    deactivateAccount,
    getAccount,
    GetAccountResponse,
    removeAccessLevel,
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
import { validationContext } from "../../context/validationContext";
import InputWithValidation from "../shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../shared/ValidationMessage/ValidationMessage";
import { set } from "immer/dist/internal";

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
    const [accessLevelToProcess, setAccessLevelToProcess] =
        useState<AccessLevelType>("ADMINISTRATOR");
    const [isAccountBlockModalOpen, setAccountBlockModalOpen] =
        useState<boolean>(false);
    const [isAccountRemoveAccessLevelOpen, setAccountRemoveAccessLevelOpen] =
        useState<boolean>(false);
    const [isAddAccessLevelModalOpen, setAddAccessLevelModalOpen] =
        useState<boolean>(false);
    const accountsAccessLevels =
        account?.accessLevels.map((level) => level.level) ?? [];
    const navigate = useNavigate();

    const handleAccessLevelClick = async (
        level: AccessLevelType,
        alreadyHas: boolean
    ) => {
        setAccessLevelToProcess(level);
        if (alreadyHas) {
            setAccountRemoveAccessLevelOpen(true);
        } else {
            setAddAccessLevelModalOpen(true);
        }
    };

    const handleRemoveAccessLevel = async () => {
        setLoading({ ...loading, actionLoading: true });
        const level = accessLevelToProcess;
        if (!account) return;
        const response = await removeAccessLevel(account.login, {
            etag: account.etag,
            level,
        });
        if ("errorMessage" in response) {
            setLoading({ ...loading, actionLoading: false });
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        showNotification(
            successNotficiationItems("Poziom dostępu został usunięty")
        );
        setAccount(response);
        setLoading({ ...loading, actionLoading: false });
    };

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
        if (!account) return;
        setLoading({ ...loading, actionLoading: true });
        if (deactivate) {
            const response = await deactivateAccount(
                account?.login as string,
                account?.etag as string
            );
            setLoading({ ...loading, actionLoading: false });
            if ("errorMessage" in response) {
                showNotification(
                    failureNotificationItems(response.errorMessage)
                );
                onClose();
                return;
            }
            showNotification(
                successNotficiationItems("Konto zostało deaktywowane")
            );
            onClose();
            return;
        } else {
            const response = await activateAccount(account.login, account.etag);
            if ("errorMessage" in response) {
                setLoading({ ...loading, actionLoading: false });
                showNotification(
                    failureNotificationItems(response.errorMessage)
                );
                onClose();
                return;
            }
            setLoading({ ...loading, actionLoading: false });
            showNotification(
                successNotficiationItems("Konto zostało aktywowane")
            );
            onClose();
            setLoading({ pageLoading: false });
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
    }, [isOpened]);

    return (
        <ReactModal isOpen={isOpened} style={customStyles} ariaHideApp={false}>
            <section className={styles.account_details_page}>
                {loading.pageLoading ? (
                    <ReactLoading
                        type="cylon"
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

                                {["ADMINISTRATOR", "CLIENT", "SPECIALIST"].map(
                                    (entry) => (
                                        <AccessLevel
                                            key={entry}
                                            accessLevel={
                                                entry as AccessLevelType
                                            }
                                            clickable={true}
                                            selectable={false}
                                            grayed={
                                                !accountsAccessLevels.includes(
                                                    entry as AccessLevelType
                                                )
                                            }
                                            onClick={() => {
                                                handleAccessLevelClick(
                                                    entry as AccessLevelType,
                                                    accountsAccessLevels.includes(
                                                        entry as AccessLevelType
                                                    )
                                                );
                                            }}
                                        />
                                    )
                                )}
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
                                        setAccountBlockModalOpen(true);
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
                            isLoading={loading.actionLoading as boolean}
                            isOpened={isAccountBlockModalOpen}
                            handleFunction={async () => {
                                await handleDeactivateActivateAccount(
                                    account?.active as boolean
                                );
                                setAccountBlockModalOpen(false);
                            }}
                            onClose={() => {
                                setAccountBlockModalOpen(false);
                            }}
                        >
                            Czy na pewno chcesz{" "}
                            {account?.active ? "zablokować" : "odblokować"}{" "}
                            konto? Operacja jest nieodwracalna.
                        </ConfirmActionModal>
                        <ConfirmActionModal
                            title="Usuń poziom dostępu"
                            isLoading={loading.actionLoading as boolean}
                            isOpened={isAccountRemoveAccessLevelOpen}
                            handleFunction={async () => {
                                await handleRemoveAccessLevel();
                                setAccountRemoveAccessLevelOpen(false);
                            }}
                            onClose={() => {
                                setAccountRemoveAccessLevelOpen(false);
                            }}
                        >
                            Czy na pewno chcesz usunąć poziom dostępu ? Operacja
                            jest nieodwracalna.
                        </ConfirmActionModal>
                        <AddAccessLevelModal
                            isLoading={loading.actionLoading as boolean}
                            isOpened={isAddAccessLevelModalOpen}
                            account={account}
                            onClose={() => {
                                setAddAccessLevelModalOpen(false);
                            }}
                            setAccount={async (account: GetAccountResponse) => {
                                setAccount(account);
                            }}
                            accessLevel={accessLevelToProcess}
                        />
                    </div>
                )}
            </section>
        </ReactModal>
    );
};

export default AccountDetails;

const AddAccessLevelModal = ({
    isLoading,
    isOpened,
    onClose,
    setAccount,
    accessLevel,
    account,
}: {
    isLoading: boolean;
    isOpened: boolean;
    onClose: () => void;
    setAccount: (account: AddAccessLevelResponse) => void;
    accessLevel: AccessLevelType;
    account: GetAccountResponse | undefined;
}) => {
    const [accessLevelToAdd, setAccessLevelToAdd] = useState<AccessLevel>({
        level: accessLevel,
        pesel: "",
        phoneNumber: "",
        contactEmail: "",
    });

    const {
        state,
        state: {
            isPhoneNumberValidAdministrator,
            isPhoneNumberValidSpecialist,
            isPhoneNumberValidClient,
            isPESELValid,
            isEmailValidAdministrator,
            isEmailValidSpecialist,
        },
        dispatch,
    } = useContext(validationContext);

    const handleSubmit = async () => {
        if (!account) return;
        const response = await addAccesLevel(account.login, {
            ...accessLevelToAdd,
            level: accessLevel,
            etag: account.etag,
        });
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        showNotification(
            successNotficiationItems("Poziom dostępu został dodany")
        );
        setAccount(response);
        onClose();
    };

    return (
        <ConfirmActionModal
            title={`Dodaj poziom dostępu ${accessLevel}`}
            isLoading={isLoading}
            isOpened={isOpened}
            handleFunction={handleSubmit}
            onClose={onClose}
        >
            <div className={styles.edit_fields_wrapper}>
                {accessLevel === "CLIENT" && (
                    <>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title="Numer PESEL: "
                                value={accessLevelToAdd.pesel}
                                validationType="VALIDATE_PESEL"
                                isValid={isPESELValid}
                                onChange={(e) => {
                                    if (e.target.value)
                                        setAccessLevelToAdd((old) => {
                                            old.pesel = e.target.value;
                                            return old;
                                        });
                                }}
                            />
                            <div />
                            <ValidationMessage
                                isValid={isPESELValid}
                                message="Numer pesel musi składać się z 11 cyfr."
                            />
                        </div>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title="Numer telefonu: "
                                value={accessLevelToAdd.phoneNumber}
                                validationType="VALIDATE_PHONENUMBER_CLIENT"
                                isValid={isPhoneNumberValidClient}
                                onChange={(e) => {
                                    if (e.target.value)
                                        setAccessLevelToAdd((old) => {
                                            old.phoneNumber = e.target.value;
                                            return old;
                                        });
                                }}
                            />
                            <div />
                            <ValidationMessage
                                isValid={isPhoneNumberValidClient}
                                message="Numer telefonu musi składać się z 9 cyfr."
                            />
                        </div>
                    </>
                )}

                {["ADMINISTRATOR", "SPECIALIST"].includes(accessLevel) && (
                    <>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title="Numer telefonu: "
                                value={accessLevelToAdd.phoneNumber}
                                validationType="VALIDATE_PHONENUMBER_ADMINISTRATOR"
                                isValid={isPhoneNumberValidAdministrator}
                                onChange={(e) => {
                                    setAccessLevelToAdd((old) => {
                                        old.phoneNumber = e.target.value;
                                        return old;
                                    });
                                }}
                            />
                            <ValidationMessage
                                isValid={isPhoneNumberValidAdministrator}
                                message="Numer telefonu musi składać się z 9 cyfr."
                            />
                        </div>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title="Email kontaktowy: "
                                value={accessLevelToAdd.contactEmail}
                                validationType="VALIDATE_EMAIL_ADMINISTRATOR"
                                isValid={isEmailValidAdministrator}
                                onChange={(e) => {
                                    setAccessLevelToAdd((old) => {
                                        old.contactEmail = e.target.value;
                                        return old;
                                    });
                                }}
                            />
                            <ValidationMessage
                                isValid={isEmailValidAdministrator}
                                message="Email musi być poprawny."
                            />
                        </div>
                    </>
                )}
            </div>
        </ConfirmActionModal>
    );
};

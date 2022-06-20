import { useState, useEffect, useContext } from "react";
import { useNavigate, useParams } from "react-router";
import style from "./userDetails.module.scss";
import { useTranslation } from "react-i18next";
import {
    editAnyAccount,
    getAccount,
    GetAccountResponse,
} from "../../../../api";
import ReactLoading from "react-loading";
import { failureNotificationItems } from "../../../../utils/showNotificationsItems";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import avatar from "../../../../assets/images/avatar.jpg";
import { faCancel, faCheck, faEdit } from "@fortawesome/free-solid-svg-icons";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import { validationContext } from "../../../../context/validationContext";
import { showNotification } from "@mantine/notifications";
import { successNotficiationItems } from "../../../../utils/showNotificationsItems";
import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";

const EditAnyAccountPage = () => {
    const [account, setAccount] = useState<GetAccountResponse>();
    const accountAccessLevels = account?.accessLevels.map(
        (level) => level.level
    );
    const [opened, setOpened] = useState(false);
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const navigate = useNavigate();
    const { login } = useParams();
    const { t } = useTranslation();
    const {
        state,
        state: {
            isFirstNameValid,
            isLastNameValid,
            isPhoneNumberValidAdministrator,
            isPhoneNumberValidSpecialist,
            isPhoneNumberValidClient,
            isPESELValid,
            isEmailValidAdministrator,
            isEmailValidSpecialist,
        },
        dispatch,
    } = useContext(validationContext);

    useEffect(() => {
        handleGetUser();
    }, []);

    const handleGetUser = async () => {
        if (!login) return;
        setLoading({ ...loading, pageLoading: true });
        const response = await getAccount(login);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            setLoading({ ...loading, pageLoading: false });
            return;
        }
        setAccount(response);
        setLoading({ ...loading, pageLoading: false });
    };

    const handleSubmit = async () => {
        setLoading({ ...loading, actionLoading: true });
        if (!account || !login) return;
        const response = await editAnyAccount(login, account);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            setLoading({ ...loading, actionLoading: false });
            return;
        }
        showNotification(successNotficiationItems(t("account.edit.success")));
        setAccount(response);
        setLoading({ ...loading, actionLoading: false });
        navigate("/accounts");
    };

    const isEveryFieldValid =
        isFirstNameValid &&
        isLastNameValid &&
        (accountAccessLevels?.includes("ADMINISTRATOR")
            ? isPhoneNumberValidAdministrator && isEmailValidAdministrator
            : true) &&
        (accountAccessLevels?.includes("SPECIALIST")
            ? isPhoneNumberValidSpecialist && isEmailValidSpecialist
            : true) &&
        (accountAccessLevels?.includes("CLIENT")
            ? isPhoneNumberValidClient && isPESELValid
            : true);

    return (
        <section className={style.edit_own_account_page}>
            {loading.pageLoading ? (
                <ReactLoading
                    type="cylon"
                    color="#fff"
                    width="10rem"
                    height="10rem"
                />
            ) : (
                <>
                    <div className={style.edit_own_account_header}>
                        <h2>Edytuj dane własnego konta</h2>
                    </div>
                    <div className={style.edit_own_account_content}>
                        <div className={style.edit_data_account_wrapper}>
                            <div className={style.avatar_wrapper}>
                                <img
                                    src={avatar}
                                    alt="User avatar"
                                    className={style.change_avatar}
                                />
                                <div className={style.edit_avatar_icon_wrapper}>
                                    <FontAwesomeIcon
                                        icon={faEdit}
                                        className={style.edit_avatar_icon}
                                    />
                                </div>
                            </div>
                            <div className={style.edit_fields_wrapper}>
                                <div className={style.edit_field}>
                                    <InputWithValidation
                                        title="Imię: "
                                        value={account?.firstName}
                                        validationType="VALIDATE_FIRSTNAME"
                                        isValid={isFirstNameValid}
                                        onChange={(e) => {
                                            if (e.target.value && account)
                                                setAccount({
                                                    ...account,
                                                    firstName: e.target.value,
                                                });
                                        }}
                                    />
                                    <ValidationMessage
                                        isValid={isFirstNameValid}
                                        message="Imię musi składać się z co najmniej 3 liter."
                                    />
                                </div>
                                <div className={style.edit_field}>
                                    <InputWithValidation
                                        title="Nazwisko: "
                                        value={account?.lastName}
                                        validationType="VALIDATE_LASTNAME"
                                        isValid={isLastNameValid}
                                        onChange={(e) => {
                                            if (e.target.value && account)
                                                setAccount({
                                                    ...account,
                                                    lastName: e.target.value,
                                                });
                                        }}
                                    />
                                    <ValidationMessage
                                        isValid={isLastNameValid}
                                        message="Nazwisko musi składać się z co najmniej 3 liter."
                                    />
                                </div>

                                {accountAccessLevels?.includes("CLIENT") ? (
                                    <>
                                        <div
                                            className={
                                                style.access_level_name_header
                                            }
                                        >
                                            CLIENT
                                        </div>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title="Numer PESEL: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                "CLIENT"
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.pesel
                                                        )[0]
                                                }
                                                validationType="VALIDATE_PESEL"
                                                isValid={isPESELValid}
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        "CLIENT"
                                                                    ) {
                                                                        level.pesel =
                                                                            e.target.value;
                                                                    }
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={isPESELValid}
                                                message="Numer pesel musi składać się z 11 cyfr."
                                            />
                                        </div>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title="Numer telefonu: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                "CLIENT"
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.phoneNumber
                                                        )[0]
                                                }
                                                validationType="VALIDATE_PHONENUMBER_CLIENT"
                                                isValid={
                                                    isPhoneNumberValidClient
                                                }
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        "CLIENT"
                                                                    )
                                                                        level.phoneNumber =
                                                                            e.target.value;
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={
                                                    isPhoneNumberValidClient
                                                }
                                                message="Numer telefonu musi składać się z 9 cyfr."
                                            />
                                        </div>
                                    </>
                                ) : null}

                                {accountAccessLevels?.includes(
                                    "ADMINISTRATOR"
                                ) ? (
                                    <>
                                        <div
                                            className={
                                                style.access_level_name_header
                                            }
                                        >
                                            ADMINISTRATOR
                                        </div>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title="Numer telefonu: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                "ADMINISTRATOR"
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.phoneNumber
                                                        )[0]
                                                }
                                                validationType="VALIDATE_PHONENUMBER_ADMINISTRATOR"
                                                isValid={
                                                    isPhoneNumberValidAdministrator
                                                }
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        "ADMINISTRATOR"
                                                                    )
                                                                        level.phoneNumber =
                                                                            e.target.value;
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={
                                                    isPhoneNumberValidAdministrator
                                                }
                                                message="Numer telefonu musi składać się z 9 cyfr."
                                            />
                                        </div>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title="Email kontaktowy: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                "ADMINISTRATOR"
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.contactEmail
                                                        )[0]
                                                }
                                                validationType="VALIDATE_EMAIL_ADMINISTRATOR"
                                                isValid={
                                                    isEmailValidAdministrator
                                                }
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        "ADMINISTRATOR"
                                                                    )
                                                                        level.contactEmail =
                                                                            e.target.value;
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={
                                                    isEmailValidAdministrator
                                                }
                                                message="Email musi być poprawny."
                                            />
                                        </div>
                                    </>
                                ) : null}

                                {accountAccessLevels?.includes("SPECIALIST") ? (
                                    <>
                                        <div
                                            className={
                                                style.access_level_name_header
                                            }
                                        >
                                            SPECIALIST
                                        </div>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title="Numer telefonu: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                "SPECIALIST"
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.phoneNumber
                                                        )[0]
                                                }
                                                validationType="VALIDATE_PHONENUMBER_SPECIALIST"
                                                isValid={
                                                    isPhoneNumberValidSpecialist
                                                }
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        "SPECIALIST"
                                                                    )
                                                                        level.phoneNumber =
                                                                            e.target.value;
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={
                                                    isPhoneNumberValidSpecialist
                                                }
                                                message="Numer telefonu musi składać się z 9 cyfr."
                                            />
                                        </div>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title="Email kontaktowy: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                "SPECIALIST"
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.contactEmail
                                                        )[0]
                                                }
                                                validationType="VALIDATE_EMAIL_SPECIALIST"
                                                isValid={isEmailValidSpecialist}
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        "SPECIALIST"
                                                                    )
                                                                        level.contactEmail =
                                                                            e.target.value;
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={isEmailValidSpecialist}
                                                message="Email musi być poprawny."
                                            />
                                        </div>
                                    </>
                                ) : null}
                            </div>
                            <div className={style.edit_data_buttons_wrapper}>
                                <ActionButton
                                    onClick={() => {
                                        setOpened(true);
                                    }}
                                    isDisabled={!isEveryFieldValid}
                                    icon={faCheck}
                                    color="green"
                                    title="Zatwierdź"
                                />
                                <ActionButton
                                    onClick={() => {
                                        navigate("/accounts");
                                    }}
                                    icon={faCancel}
                                    color="red"
                                    title="Anuluj"
                                />
                            </div>
                        </div>
                    </div>
                </>
            )}
            <ConfirmActionModal
                isOpened={opened}
                onClose={() => {
                    setOpened(false);
                }}
                handleFunction={async () => {
                    await handleSubmit();
                    setOpened(false);
                }}
                isLoading={loading.actionLoading ?? false}
                title="Edycja danych innego konta"
            >
                Czy na pewno chcesz zmienić dane tego konta? Operacja jest
                nieodwracalna
            </ConfirmActionModal>
        </section>
    );
};

export default EditAnyAccountPage;

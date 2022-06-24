import { useContext, useEffect, useState } from "react";
import avatar from "../../../../assets/images/avatar.jpg";
import style from "./style.module.scss";
import { faCancel, faCheck, faEdit } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useNavigate } from "react-router-dom";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import { validationContext } from "../../../../context/validationContext";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import {
    editOwnAccount,
    GetAccountResponse,
    getOwnAccount,
} from "../../../../api";
import ReactLoading from "react-loading";
import {
    GoogleReCaptchaProvider,
    useGoogleReCaptcha,
} from "react-google-recaptcha-v3";
import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";
import { showNotification } from "@mantine/notifications";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import { useTranslation } from "react-i18next";

const EditOwnAccountPageInternal = () => {
    const [account, setAccount] = useState<GetAccountResponse>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [error, setError] = useState<ApiError>();
    const [opened, setOpened] = useState<boolean>(false);
    const { executeRecaptcha } = useGoogleReCaptcha();

    const { t } = useTranslation();

    const accessLevel = useStoreSelector((state) => state.user.cur);

    const {
        state,
        state: {
            isFirstNameValid,
            isLastNameValid,
            isPhoneNumberValidAdministrator,
            isPhoneNumberValidClient,
            isPESELValid,
            isEmailValidAdministrator,
        },
        dispatch,
    } = useContext(validationContext);

    const navigate = useNavigate();

    const handleGetOwnAccount = async () => {
        try {
            const data = await getOwnAccount();
            if ("errorMessage" in data) return;
            setAccount(data);
            setLoading({ ...loading, pageLoading: false });
        } catch (error: ApiError | any) {
            setLoading({ ...loading, pageLoading: false });
            setError(error);
            showNotification(failureNotificationItems(error?.errorMessage));
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    const handleSubmit = async () => {
        if (!account || !executeRecaptcha) return;
        setLoading({ ...loading, actionLoading: true });

        const captcha = await executeRecaptcha("edit_account");

        const request = {
            ...account,
            captcha,
        };
        const response = await editOwnAccount(request);
        if ("errorMessage" in response) {
            setLoading({ ...loading, actionLoading: false });
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        setAccount(response);
        navigate("/account");
        setLoading({ ...loading, actionLoading: false });
        showNotification(
            successNotficiationItems(t("editOwnAccountPage.updateSuccessfully"))
        );
    };

    useEffect(() => {
        dispatch({ type: "RESET_VALIDATION", payload: { ...state } });
        handleGetOwnAccount();
    }, []);

    const isEveryFieldValid =
        isFirstNameValid &&
        isLastNameValid &&
        isPhoneNumberValidAdministrator &&
        isPhoneNumberValidClient &&
        isPESELValid &&
        isEmailValidAdministrator;

    return (
        <section className={style.edit_own_account_page}>
            {loading.pageLoading ? (
                <ReactLoading
                    type="bars"
                    color="#fff"
                    width="10rem"
                    height="10rem"
                    className={style.loading}
                />
            ) : (
                <>
                    <div className={style.edit_own_account_header}>
                        <h2>{t("editOwnAccountPage.editOwnAccount")}</h2>
                    </div>
                    <div className={style.edit_own_account_content}>
                        <div className={style.edit_data_account_wrapper}>
                            <div className={style.avatar_wrapper}>
                                <img
                                    src={account?.url}
                                    alt="User avatar"
                                    className={style.change_avatar}
                                />
                                {/* <div className={style.edit_avatar_icon_wrapper}>
                                    <FontAwesomeIcon
                                        icon={faEdit}
                                        className={style.edit_avatar_icon}
                                    />
                                </div> */}
                            </div>
                            <div className={style.edit_fields_wrapper}>
                                <div className={style.edit_field}>
                                    <InputWithValidation
                                        title={t(
                                            "editOwnAccountPage.firstName"
                                        )}
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
                                        message={t(
                                            "editOwnAccountPage.firstNameMsg"
                                        )}
                                    />
                                </div>
                                <div className={style.edit_field}>
                                    <InputWithValidation
                                        title={t("editOwnAccountPage.lastName")}
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
                                        message={t(
                                            "editOwnAccountPage.lastNameMsg"
                                        )}
                                    />
                                </div>

                                {accessLevel === "CLIENT" ? (
                                    <>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title="PESEL"
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
                                                message={t(
                                                    "editOwnAccountPage.peselMsg"
                                                )}
                                            />
                                        </div>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title={t(
                                                    "editOwnAccountPage.phoneNumber"
                                                )}
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                accessLevel
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
                                                                        accessLevel
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
                                                message={t(
                                                    "editOwnAccountPage.phoneNumberMsg"
                                                )}
                                            />
                                        </div>
                                    </>
                                ) : null}

                                {["ADMINISTRATOR", "SPECIALIST"].includes(
                                    accessLevel
                                ) ? (
                                    <>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title={t(
                                                    "editOwnAccountPage.phoneNumber"
                                                )}
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                accessLevel
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
                                                                        accessLevel
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
                                                message={t(
                                                    "editOwnAccountPage.phoneNumberMsg"
                                                )}
                                            />
                                        </div>
                                        <div className={style.edit_field}>
                                            <InputWithValidation
                                                title={t(
                                                    "editOwnAccountPage.email"
                                                )}
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                accessLevel
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
                                                                        accessLevel
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
                                                message={t(
                                                    "editOwnAccountPage.emailMsg"
                                                )}
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
                                    title={t("editOwnAccountPage.confirm")}
                                />
                                <ActionButton
                                    onClick={() => {
                                        navigate("/account");
                                    }}
                                    icon={faCancel}
                                    color="red"
                                    title={t("editOwnAccountPage.cancel")}
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
                isLoading={loading.actionLoading as boolean}
                title={t("editOwnAccountPage.editOwnAccount")}
            >
                {t("editOwnAccountPage.confirmation")}
            </ConfirmActionModal>
        </section>
    );
};

const EditOwnAccountPage = () => {
    return (
        <GoogleReCaptchaProvider reCaptchaKey="6Lf85hEgAAAAAONrGfo8SoQSb9GmfzHXTTgjKJzT">
            <EditOwnAccountPageInternal />
        </GoogleReCaptchaProvider>
    );
};

export default EditOwnAccountPage;

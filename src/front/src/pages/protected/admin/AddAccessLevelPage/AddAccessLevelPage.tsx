import { showNotification } from "@mantine/notifications";
import { useContext, useState } from "react";
import {
    addAccesLevel,
    AddAccessLevelResponse,
    GetAccountResponse,
} from "../../../../api";
import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../../context/validationContext";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import styles from "./style.module.scss";
import { useTranslation } from "react-i18next";

const AddAccessLevelPage = ({
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

    const { t } = useTranslation();
    const {
        state: {
            isPhoneNumberValidAdministrator,
            isPhoneNumberValidClient,
            isPESELValid,
            isEmailValidAdministrator,
        },
    } = useContext(validationContext);

    const getAccessLevel = (accessLevel: AccessLevelType) => {
        switch (accessLevel) {
            case "ADMINISTRATOR": {
                return t("addAccessLevelPage.admin");
            }
            case "SPECIALIST": {
                return t("addAccessLevelPage.spec");
            }
            case "CLIENT": {
                return t("addAccessLevelPage.client");
            }
            default: {
                return "";
            }
        }
    };

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
            successNotficiationItems(t("addAccessLevelPage.added"))
        );
        setAccount(response);
        setAccessLevelToAdd({
            level: accessLevel,
            pesel: "",
            phoneNumber: "",
            contactEmail: "",
        });
        onClose();
    };

    return (
        <ConfirmActionModal
            title={`${t("addAccessLevelPage.addAccess")} ${getAccessLevel(
                accessLevel
            )}`}
            isLoading={isLoading}
            isOpened={isOpened}
            handleFunction={handleSubmit}
            onClose={onClose}
            size={1000}
        >
            <div className={styles.edit_fields_wrapper}>
                {accessLevel === "CLIENT" && (
                    <>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title="PESEL"
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
                                required
                            />
                            <div />
                            <ValidationMessage
                                isValid={isPESELValid}
                                message={t("addAccessLevelPage.peselMsg")}
                            />
                        </div>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title={t("addAccessLevelPage.phoneNumber")}
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
                                required
                            />
                            <div />
                            <ValidationMessage
                                isValid={isPhoneNumberValidClient}
                                message={t("addAccessLevelPage.phoneNumberMsg")}
                            />
                        </div>
                    </>
                )}

                {["ADMINISTRATOR", "SPECIALIST"].includes(accessLevel) && (
                    <>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title={t("addAccessLevelPage.phoneNumber")}
                                value={accessLevelToAdd.phoneNumber}
                                validationType="VALIDATE_PHONENUMBER_ADMINISTRATOR"
                                isValid={isPhoneNumberValidAdministrator}
                                onChange={(e) => {
                                    setAccessLevelToAdd((old) => {
                                        old.phoneNumber = e.target.value;
                                        return old;
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isPhoneNumberValidAdministrator}
                                message={t("addAccessLevelPage.phoneNumberMsg")}
                            />
                        </div>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title={t("addAccessLevelPage.email")}
                                value={accessLevelToAdd.contactEmail}
                                validationType="VALIDATE_EMAIL_ADMINISTRATOR"
                                isValid={isEmailValidAdministrator}
                                onChange={(e) => {
                                    setAccessLevelToAdd((old) => {
                                        old.contactEmail = e.target.value;
                                        return old;
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isEmailValidAdministrator}
                                message={t("addAccessLevelPage.emailMsg")}
                            />
                        </div>
                    </>
                )}
            </div>
        </ConfirmActionModal>
    );
};

export default AddAccessLevelPage;

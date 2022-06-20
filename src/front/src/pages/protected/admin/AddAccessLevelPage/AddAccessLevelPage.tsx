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

const getAccessLevel = (accessLevel: AccessLevelType) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return "administratora";
        }
        case "SPECIALIST": {
            return "specjalisty";
        }
        case "CLIENT": {
            return "klienta";
        }
        default: {
            return "";
        }
    }
};

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

    const {
        state: {
            isPhoneNumberValidAdministrator,
            isPhoneNumberValidClient,
            isPESELValid,
            isEmailValidAdministrator,
        },
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
            title={`Dodaj poziom dostępu ${getAccessLevel(accessLevel)}`}
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
                                title="Numer PESEL"
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
                                message="Numer pesel musi składać się z 11 cyfr."
                            />
                        </div>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title="Numer telefonu"
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
                                message="Numer telefonu musi składać się z 9 cyfr."
                            />
                        </div>
                    </>
                )}

                {["ADMINISTRATOR", "SPECIALIST"].includes(accessLevel) && (
                    <>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title="Numer telefonu"
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
                                message="Numer telefonu musi składać się z 9 cyfr."
                            />
                        </div>
                        <div className={styles.edit_field}>
                            <InputWithValidation
                                title="Email kontaktowy"
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
                                message="Email musi być poprawny."
                            />
                        </div>
                    </>
                )}
            </div>
        </ConfirmActionModal>
    );
};

export default AddAccessLevelPage;

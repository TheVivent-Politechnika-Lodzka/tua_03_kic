import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import {
    changeOwnPassword,
    ChangeOwnPasswordRequest,
    GetAccountResponse,
    getOwnAccount,
} from "../../../../api";
import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../../context/validationContext";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import ReactLoading from "react-loading";
import styles from "./style.module.scss";
import Modal from "../../../../components/shared/Modal/Modal";
import {
    GoogleReCaptchaProvider,
    useGoogleReCaptcha,
} from "react-google-recaptcha-v3";
import { display, style } from "@mui/system";

interface ChangeOwnPasswordPageProps {
    isOpen: boolean;
    onClose: () => void;
}

const ChangeOwnPasswordInternal = ({
    isOpen,
    onClose,
}: ChangeOwnPasswordPageProps) => {
    const [account, setAccount] = useState<GetAccountResponse>();
    const [password, setPassword] = useState({
        oldPassword: "",
        newPassword: "",
    });
    const [opened, setOpened] = useState<boolean>(false);
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });

    const { executeRecaptcha } = useGoogleReCaptcha();

    const {
        state,
        state: { isOldPasswordValid, isNewPasswordValid },
        dispatch,
    } = useContext(validationContext);

    const { t } = useTranslation();

    const handleGetOwnAccount = async () => {
        const response = await getOwnAccount();
        if ("errorMessage" in response) {
            setLoading({ ...loading, pageLoading: false });
            showNotification(failureNotificationItems(response?.errorMessage));
            return;
        }
        setAccount(response);
        setLoading({ ...loading, pageLoading: false });
    };

    const handleChangeOwnPassword = async () => {
        if (!account || !executeRecaptcha) return;
        setLoading({ ...loading, actionLoading: true });

        const captcha = await executeRecaptcha("change_own_password");

        const response = await changeOwnPassword({
            oldPassword: password.oldPassword,
            newPassword: password.newPassword,
            id: account?.id,
            version: account?.version,
            etag: account?.etag,
            captcha: captcha,
        });
        if ("errorMessage" in response) {
            setLoading({ ...loading, actionLoading: false });
            showNotification(failureNotificationItems(response?.errorMessage));
            return;
        }
        setLoading({ ...loading, actionLoading: false });
        setOpened(false);
        onClose();
        setPassword({ oldPassword: "", newPassword: "" });
        dispatch({ type: "RESET_VALIDATION", payload: { ...state } });
        showNotification(
            successNotficiationItems(`Hasło zostało pomyślnie zmienione`)
        );
    };

    const arePasswordsValid = isOldPasswordValid && isNewPasswordValid;
    const arePasswordsSame = password.newPassword === password.oldPassword;

    useEffect(() => {
        handleGetOwnAccount();
        dispatch({ type: "RESET_VALIDATION", payload: { ...state } });
    }, []);

    useEffect(() => {
        handleGetOwnAccount();
    }, [opened]);

    return (
        <Modal isOpen={isOpen}>
            <div className={styles.change_own_password_page}>
                {loading.pageLoading ? (
                    <ReactLoading
                        type="bars"
                        color="#fff"
                        width="10rem"
                        height="10rem"
                    />
                ) : (
                    <>
                        <div className={styles.title_wrapper}>
                            <h2>Zmień swoje hasło</h2>
                        </div>
                        <div className={styles.content}>
                            <div className={styles.change_password_wrapper}>
                                <div className={styles.input_wrapper}>
                                    <InputWithValidation
                                        title="Stare hasło: "
                                        validationType="VALIDATE_OLD_PASSWORD"
                                        isValid={isOldPasswordValid}
                                        value={password.oldPassword}
                                        onChange={(e) =>
                                            setPassword({
                                                ...password,
                                                oldPassword: e.target.value,
                                            })
                                        }
                                        type="password"
                                    />
                                    <InputWithValidation
                                        title="Nowe hasło: "
                                        validationType="VALIDATE_NEW_PASSWORD"
                                        isValid={isNewPasswordValid}
                                        value={password.newPassword}
                                        onChange={(e) =>
                                            setPassword({
                                                ...password,
                                                newPassword: e.target.value,
                                            })
                                        }
                                        type="password"
                                    />
                                </div>
                                <div className={styles.action_buttons_wrapper}>
                                    <ActionButton
                                        onClick={() => {
                                            setOpened(true);
                                        }}
                                        isDisabled={
                                            !arePasswordsValid ||
                                            arePasswordsSame
                                        }
                                        icon={faCheck}
                                        color="green"
                                        title="Zatwierdź"
                                    />
                                    <ActionButton
                                        onClick={onClose}
                                        icon={faCancel}
                                        color="red"
                                        title="Anuluj"
                                    />
                                </div>
                            </div>
                            <div className={styles.validation_status_wrapper}>
                                <ValidationMessage
                                    isValid={arePasswordsValid}
                                    message="Oba hasła muszą być dłuższe niż 8 znaków oraz muszą zawierać jedną dużą literę, jedną cyfrę i jeden znak specjalny"
                                />
                                <ValidationMessage
                                    isValid={!arePasswordsSame}
                                    message="Hasła nie mogą być takie same"
                                />
                            </div>
                        </div>

                        <ConfirmActionModal
                            isOpened={opened}
                            onClose={() => {
                                setOpened(false);
                            }}
                            handleFunction={async () => {
                                await handleChangeOwnPassword();
                                setOpened(false);
                            }}
                            isLoading={loading.actionLoading as boolean}
                            title={`Zmiana swojego hasła`}
                        >
                            Czy na pewno chcesz zmienić hasło? Operacja jest
                            nieodwracalna
                        </ConfirmActionModal>
                    </>
                )}
            </div>
        </Modal>
    );
};

const ChangeOwnPasswordPage = ({
    isOpen,
    onClose,
}: ChangeOwnPasswordPageProps) => {
    return (
        <GoogleReCaptchaProvider reCaptchaKey="6Lf85hEgAAAAAONrGfo8SoQSb9GmfzHXTTgjKJzT">
            <ChangeOwnPasswordInternal isOpen={isOpen} onClose={onClose} />
        </GoogleReCaptchaProvider>
    );
};

export default ChangeOwnPasswordPage;

import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router";
import {
    changeAnyPassword,
    getAccount,
    GetAccountResponse,
} from "../../../../api";
import ConfirmActionModal from "../../../../components/ConfirmActionModal/ConfirmActionModal";
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
import { Modal } from "react-bootstrap";

interface ChangeUserPasswordPageProps {
    isOpen: boolean;
    onClose?: () => void;
}

const ChangeUserPasswordPage = ({isOpen, onClose}: ChangeUserPasswordPageProps) => {
    const [account, setAccount] = useState<GetAccountResponse>();
    const [newPassword, setNewPassword] = useState<string>("");
    const [opened, setOpened] = useState<boolean>(false);
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });

    const {
        state,
        state: { isNewPasswordValid },
        dispatch,
    } = useContext(validationContext);

    const { t } = useTranslation();
    const navigate = useNavigate();
    const { login } = useParams();

    const handleGetOwnAccount = async () => {
        try {
            const data = await getAccount(login as string);
            if ("errorMessage" in data) return;
            setAccount(data);
            setLoading({ ...loading, pageLoading: false });
        } catch (error: ApiError | any) {
            setLoading({ ...loading, pageLoading: false });
            showNotification(failureNotificationItems(error?.errorMessage));
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    const handleChangeUserPassword = async () => {
        setLoading({ ...loading, actionLoading: true });
        if (!account) return;
        const response = await changeAnyPassword(login as string, {
            newPassword: newPassword,
            id: account?.id,
            version: account?.version,
            etag: account?.etag,
        });
        if ("errorMessage" in response) {
            setLoading({ ...loading, actionLoading: false });
            showNotification(failureNotificationItems(response?.errorMessage));
            return;
        }
        setLoading({ ...loading, actionLoading: false });
        setOpened(false);
        showNotification(
            successNotficiationItems(
                `Hasło użytkownika ${login} zostało pomyślnie zmienione`
            )
        );
        navigate("/accounts");
    };

    useEffect(() => {
        handleGetOwnAccount();
        dispatch({ type: "RESET_VALIDATION", payload: { ...state } });
    }, []);

    return (
        <Modal isOpen={isOpen} className={styles.change_user_password_page}>
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
                        <h2>
                            {t("ChangeAccountPassword.title")} {login}
                        </h2>
                    </div>
                    <div className={styles.content}>
                        <div className={styles.change_password_wrapper}>
                            <div className={styles.input_wrapper}>
                                <InputWithValidation
                                    title="Nowe hasło: "
                                    validationType="VALIDATE_NEW_PASSWORD"
                                    isValid={isNewPasswordValid}
                                    value={newPassword}
                                    type="password"
                                    onChange={(e) =>
                                        setNewPassword(e.target.value)
                                    }
                                />
                            </div>
                            <div className={styles.action_buttons_wrapper}>
                                <ActionButton
                                    onClick={() => {
                                        setOpened(true);
                                    }}
                                    isDisabled={!isNewPasswordValid}
                                    icon={faCheck}
                                    color="green"
                                    title="Zatwierdź"
                                />
                                <ActionButton
                                    onClick={() => {
                                        navigate("/account");
                                    }}
                                    icon={faCancel}
                                    color="red"
                                    title="Anuluj"
                                />
                            </div>
                        </div>
                        <div className={styles.validation_status_wrapper}>
                            <ValidationMessage
                                isValid={isNewPasswordValid}
                                message="Hasło musi być dłuższe niż 8 znaków oraz musi zawierać co jedną dużą literę, jedną cyfrę i jeden znak specjalny"
                            />
                        </div>
                    </div>

                    <ConfirmActionModal
                        isOpened={opened}
                        onClose={() => {
                            setOpened(false);
                        }}
                        handleFunction={async () => {
                            await handleChangeUserPassword();
                            setOpened(false);
                        }}
                        isLoading={loading.actionLoading as boolean}
                        title={`Zmiana hasła użytkownika`}
                        description={`Czy na pewno chcesz zmienić hasło użytkownika ${login}? Operacja jest nieodwracalna`}
                    />
                </>
            )}
        </Modal>
    );
};

export default ChangeUserPasswordPage;

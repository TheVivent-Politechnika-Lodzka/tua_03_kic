import { faClose, faPaperPlane } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { showNotification } from "@mantine/notifications";
import { useContext, useState } from "react";
import ReactModal from "react-modal";
import { resetPassword } from "../../../api";
import ActionButton from "../../../components/shared/ActionButton/ActionButton";
import { validationContext } from "../../../context/validationContext";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../utils/showNotificationsItems";
import style from "./style.module.scss";
import Input from "../../../components/shared/Input/Input";
import { useTranslation } from "react-i18next";

interface ResetPasswordModalInterface {
    isOpen: boolean;
    onClose: () => void;
}

export const ResetPasswordModal = ({
    isOpen,
    onClose,
}: ResetPasswordModalInterface) => {
    const [login, setLogin] = useState("");
    const [isLoading, setLoading] = useState<boolean>(false);
    const {
        state: { isLoginValid },
    } = useContext(validationContext);

    const { t } = useTranslation();

    const handleResetPassword = async () => {
        if (login === "" || login.length < 3) {
            showNotification(
                failureNotificationItems(
                    t("resetPasswordModal.failureNotificationItems")
                )
            );
            return;
        }
        setLoading(true);
        const response = await resetPassword(login);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            setLoading(false);
            return;
        }
        showNotification(
            successNotficiationItems(
                t("resetPasswordModal.successNotificationItems")
            )
        );
        setLoading(false);
        setLogin("");
        onClose();
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

    return (
        <ReactModal isOpen={isOpen} style={customStyles} ariaHideApp={false}>
            <section className={style.reset_password_modal}>
                <div className={style.content}>
                    <FontAwesomeIcon
                        className={style.close_icon}
                        icon={faClose}
                        onClick={onClose}
                    />
                    <div className={style.reset_password_content}>
                        <p className={style.title}>
                            {t("resetPasswordModal.title")}
                        </p>
                        <div className={style.edit_fields_wrapper}>
                            <Input
                                title={t("resetPasswordModal.login.title")}
                                placeholder={t(
                                    "resetPasswordModal.login.placeholder"
                                )}
                                type="text"
                                value={login}
                                onChange={(e) => setLogin(e.target.value)}
                                required={true}
                            />
                        </div>
                        <div className={style.button_wrapper}>
                            <ActionButton
                                title={t("resetPasswordModal.button")}
                                color="orange"
                                onClick={handleResetPassword}
                                isDisabled={!isLoginValid}
                                isLoading={isLoading}
                                icon={faPaperPlane}
                            />
                        </div>
                    </div>
                </div>
            </section>
        </ReactModal>
    );
};

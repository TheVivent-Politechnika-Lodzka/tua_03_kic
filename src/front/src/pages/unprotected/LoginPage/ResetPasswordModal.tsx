import { faClose, faPaperPlane } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { showNotification } from "@mantine/notifications";
import React, { useContext, useState } from "react";
import Loading from "react-loading";
import ReactModal from "react-modal";
import { resetPassword } from "../../../api";
import ReactLoading from "react-loading";
import ActionButton from "../../../components/shared/ActionButton/ActionButton";
import InputWithValidation from "../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../context/validationContext";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../utils/showNotificationsItems";
import style from "./style.module.scss";

interface ResetPasswordModalInterface {
    isOpen: boolean;
    onClose: () => void;
}

export const ResetPasswordModal = ({
    isOpen,
    onClose,
}: ResetPasswordModalInterface) => {
    const [login, setLogin] = useState("");
    const [isLoading, setLoading] = useState(false);
    const {
        state: { isLoginValid },
    } = useContext(validationContext);

    const handleResetPassword = async () => {
        if (login === "") return;
        setLoading(true);
        const response = await resetPassword(login);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            setLoading(false);
            return;
        }
        showNotification(
            successNotficiationItems(
                "Wysłano link z kodem do resetu hasła. Sprawdź maila"
            )
        );
        setLoading(false);
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
            {isLoading ? (
                <div
                    style={{
                        position: "absolute",
                        top: 0,
                        left: 0,
                        width: "100vw",
                        height: "100vh",
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "space-evenly",
                    }}
                >
                    <ReactLoading
                        type="cylon"
                        color="#fff"
                        width="10rem"
                        height="10rem"
                    />
                </div>
            ) : (
                <section className={style.reset_password_modal}>
                    <div className={style.content}>
                        <FontAwesomeIcon
                            className={style.close_icon}
                            icon={faClose}
                            onClick={onClose}
                        />
                        <div className={style.reset_password_content}>
                            <div className={style.edit_fields_wrapper}>
                                <InputWithValidation
                                    title="Login:"
                                    value={login}
                                    onChange={(e) => {
                                        setLogin(e.target.value);
                                    }}
                                    validationType="VALIDATE_LOGIN"
                                    isValid={isLoginValid}
                                    required={true}
                                    type=""
                                />
                                <ValidationMessage
                                    isValid={isLoginValid}
                                    message="Login musi składać się z x znaków"
                                />
                            </div>
                            <div className={style.button_wrapper}>
                                <ActionButton
                                    title="Przypomnij hasło"
                                    color="green"
                                    onClick={handleResetPassword}
                                    isDisabled={!isLoginValid}
                                    isLoading={false}
                                    icon={faPaperPlane}
                                />
                            </div>
                        </div>
                    </div>
                </section>
            )}
        </ReactModal>
    );
};

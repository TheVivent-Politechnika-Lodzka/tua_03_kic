import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import style from "./style.module.scss";
import { confirmResetPassword } from "../../../api";
import { showNotification } from "@mantine/notifications";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../utils/showNotificationsItems";
import InputWithValidation from "../../../components/shared/InputWithValidation/InputWithValidation";
import { validationContext } from "../../../context/validationContext";
import ValidationMessage from "../../../components/shared/ValidationMessage/ValidationMessage";
import ActionButton from "../../../components/shared/ActionButton/ActionButton";
import { faPaperPlane } from "@fortawesome/free-solid-svg-icons";

const ResetPasswordPage = () => {
    const { token } = useParams();
    const navigate = useNavigate();
    const [newPassword, setNewPassword] = useState("");
    const [newPasswordRepeat, setNewPasswordRepeat] = useState("");
    const [passwordsAreTheSame, setPasswordAreTheSame] = useState(false);
    const {
        state: { isPasswordValid },
    } = useContext(validationContext);

    useEffect(() => {
        if (newPassword === "") {
            setPasswordAreTheSame(false);
            return;
        }
        setPasswordAreTheSame(newPassword === newPasswordRepeat);
    }, [newPassword, newPasswordRepeat]);

    const handleSubmit = async () => {
        if (!token) {
            navigate("/");
            return;
        }
        if (newPassword === "") return;
        if (newPassword !== newPasswordRepeat) return;
        const response = await confirmResetPassword(token, newPassword);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        showNotification(successNotficiationItems("Pomyślnie zmieniono hasło"));
        navigate("/login");
        return;
    };

    return (
        <section className={style.edit_own_account_page}>
            <div className={style.edit_own_account_header}>
                <h2>Zresetuj hasło</h2>
            </div>
            <div className={style.edit_own_account_content}>
                <div className={style.edit_data_account_wrapper}>
                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title="Hasło: "
                                required={true}
                                value={newPassword}
                                validationType="VALIDATE_PASSWORD"
                                isValid={isPasswordValid}
                                onChange={(e) => {
                                    setNewPassword(e.target.value);
                                }}
                            />
                            <ValidationMessage
                                isValid={isPasswordValid}
                                message="Hasło musi być magiczne"
                            />
                        </div>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title="Powtórz hasło: "
                                required={true}
                                value={newPasswordRepeat}
                                validationType="VALIDATE_PASSWORD"
                                isValid={passwordsAreTheSame}
                                onChange={(e) => {
                                    setNewPasswordRepeat(e.target.value);
                                }}
                            />
                            <ValidationMessage
                                isValid={passwordsAreTheSame}
                                message="Hasła muszą być takie same"
                            />
                        </div>
                    </div>
                    <div style={{ marginBottom: "2rem" }}>
                        <ActionButton
                            isDisabled={
                                !(isPasswordValid && passwordsAreTheSame)
                            }
                            title="Zmień hasło"
                            color="green"
                            onClick={handleSubmit}
                            icon={faPaperPlane}
                        />
                    </div>
                </div>
            </div>
        </section>
    );
};

export default ResetPasswordPage;

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
import { useTranslation } from "react-i18next";

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

    const { t } = useTranslation();

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
        showNotification(
            successNotficiationItems(
                t("resetPasswordPage.successNotficiationItems")
            )
        );
        navigate("/login");
        return;
    };

    return (
        <section className={style.edit_own_account_page}>
            <div className={style.background} />
            <div className={style.content}>
                <div className={style.wrapper}>
                    <div className={style.header}>
                        <h2>{t("resetPasswordPage.header")}</h2>
                    </div>
                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("resetPasswordPage.password")}
                                required={true}
                                value={newPassword}
                                validationType="VALIDATE_PASSWORD"
                                isValid={isPasswordValid}
                                onChange={(e) => {
                                    setNewPassword(e.target.value);
                                }}
                                type="password"
                            />
                        </div>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("resetPasswordPage.repeatPassword")}
                                required={true}
                                value={newPasswordRepeat}
                                validationType="VALIDATE_PASSWORD"
                                isValid={passwordsAreTheSame}
                                onChange={(e) => {
                                    setNewPasswordRepeat(e.target.value);
                                }}
                                type="password"
                            />
                        </div>
                    </div>
                    <div style={{ marginTop: "2rem", marginBottom: "2rem" }}>
                        <ActionButton
                            isDisabled={
                                !(isPasswordValid && passwordsAreTheSame)
                            }
                            title={t("resetPasswordPage.button")}
                            color="green"
                            onClick={handleSubmit}
                            icon={faPaperPlane}
                        />
                    </div>
                </div>
                <div className={style.messages_wrapper}>
                    <ValidationMessage
                        isValid={isPasswordValid}
                        message={t("resetPasswordPage.error.password")}
                    />
                    <ValidationMessage
                        isValid={passwordsAreTheSame}
                        message={t("resetPasswordPage.error.repeatPassword")}
                    />
                </div>
            </div>
        </section>
    );
};

export default ResetPasswordPage;

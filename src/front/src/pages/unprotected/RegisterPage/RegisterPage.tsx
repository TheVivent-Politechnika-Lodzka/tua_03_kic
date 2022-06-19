import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router";
import { register, RegisterRequest } from "../../../api";
import ActionButton from "../../../components/shared/ActionButton/ActionButton";
import { failureNotificationItems } from "../../../utils/showNotificationsItems";
import styles from "./registerPage.module.scss";
import { GoogleReCaptchaProvider } from "react-google-recaptcha-v3";
import { useGoogleReCaptcha } from "react-google-recaptcha-v3";
import { validationContext } from "../../../context/validationContext";
import InputWithValidation from "../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../components/shared/ValidationMessage/ValidationMessage";
import { RegisterModal } from "../../../components/RegisterModal";

const RegisterPageInternal = () => {
    const [account, setAccount] = useState<RegisterRequest>({
        login: "",
        password: "",
        firstName: "",
        lastName: "",
        email: "",
        phoneNumber: "",
        pesel: "",
        language: {
            language: "pl",
        },
        captcha: "",
    });

    const [repeatPassword, setRepeatPassword] = useState("");
    const [repeatPasswordMatch, setRepeatPasswordMatch] =
        useState<boolean>(false);
    const { t } = useTranslation();
    const { executeRecaptcha } = useGoogleReCaptcha();
    const [opened, setOpened] = useState<boolean>(false);
    const navigate = useNavigate();
    const {
        state,
        state: {
            isFirstNameValid,
            isLastNameValid,
            isPhoneNumberValidClient,
            isPESELValid,
            isEmailValidAdministrator,
            isLoginValid,
            isPasswordValid,
        },
        dispatch,
    } = useContext(validationContext);

    const handleSubmit = async () => {
        if (executeRecaptcha === undefined) {
            return;
        }
        const captcha = await executeRecaptcha("register");
        const request = {
            ...account,
            captcha: captcha,
        };

        const response = await register(request);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        } else {
            setOpened(true);
        }
    };

    const isEveryFieldValid =
        isEmailValidAdministrator &&
        isFirstNameValid &&
        isLastNameValid &&
        isPhoneNumberValidClient &&
        isPasswordValid &&
        repeatPasswordMatch &&
        isLoginValid &&
        isPESELValid;

    useEffect(() => {
        if (
            account.password === repeatPassword &&
            account.password.length > 0 &&
            repeatPassword.length > 0
        ) {
            setRepeatPasswordMatch(true);
        } else {
            setRepeatPasswordMatch(false);
        }
    }, [account.password, repeatPassword]);

    return (
        <section className={styles.register_page}>
            <div className={styles.background} />
            <div className={styles.form_wrapper}>
                <div className={styles.create_user_page_header}>
                    {t("sign_in")}
                </div>{" "}
                <div className={styles.edit_field} title="email">
                    <InputWithValidation
                        required
                        styleWidth={{ width: "20rem" }}
                        title={t("registerPage.email")}
                        value={account.email}
                        validationType="VALIDATE_EMAIL_ADMINISTRATOR"
                        isValid={isEmailValidAdministrator}
                        onChange={(e: any) =>
                            setAccount({
                                ...account,
                                email: e.target.value,
                            })
                        }
                    />

                    <div className={styles.edit_field_validation}>
                        <ValidationMessage
                            isValid={isEmailValidAdministrator}
                            message={t("registerPage.emailMsg")}
                        />
                    </div>
                </div>
                <div className={styles.edit_field} title="firstName">
                    <InputWithValidation
                        required
                        styleWidth={{ width: "20rem" }}
                        title={t("registerPage.firstName")}
                        value={account.firstName}
                        validationType="VALIDATE_FIRSTNAME"
                        isValid={isFirstNameValid}
                        onChange={(e: any) =>
                            setAccount({
                                ...account,
                                firstName: e.target.value,
                            })
                        }
                    />
                    <div className={styles.edit_field_validation}>
                        <ValidationMessage
                            isValid={isFirstNameValid}
                            message={t("registerPage.firstNameMsg")}
                        />
                    </div>
                </div>
                <div className={styles.edit_field} title="lastName">
                    <InputWithValidation
                        required
                        styleWidth={{ width: "20rem" }}
                        title={t("registerPage.lastName")}
                        value={account.lastName}
                        validationType="VALIDATE_LASTNAME"
                        isValid={isLastNameValid}
                        onChange={(e: any) =>
                            setAccount({
                                ...account,
                                lastName: e.target.value,
                            })
                        }
                    />

                    <div className={styles.edit_field_validation}>
                        <ValidationMessage
                            isValid={isLastNameValid}
                            message={t("registerPage.lastNameMsg")}
                        />
                    </div>
                </div>
                <div className={styles.edit_field} title="pesel">
                    <InputWithValidation
                        required
                        styleWidth={{ width: "20rem" }}
                        title="PESEL "
                        value={account.pesel}
                        validationType="VALIDATE_PESEL"
                        isValid={isPESELValid}
                        onChange={(e: any) => {
                            setAccount({
                                ...account,
                                pesel: e.target.value,
                            });
                        }}
                    />

                    <div className={styles.edit_field_validation}>
                        <ValidationMessage
                            isValid={isPESELValid}
                            message={t("registerPage.peselMsg")}
                        />
                    </div>
                </div>
                <div className={styles.edit_field} title="login">
                    <InputWithValidation
                        required
                        styleWidth={{ width: "20rem" }}
                        title={t("registerPage.login")}
                        value={account.login}
                        validationType="VALIDATE_LOGIN"
                        isValid={isLoginValid}
                        onChange={(e: any) =>
                            setAccount({
                                ...account,
                                login: e.target.value,
                            })
                        }
                    />

                    <div className={styles.edit_field_validation}>
                        <ValidationMessage
                            isValid={isLoginValid}
                            message={t("registerPage.loginMsg")}
                        />
                    </div>
                </div>
                <div className={styles.edit_field} title="phoneNumber">
                    <InputWithValidation
                        required
                        styleWidth={{ width: "20rem" }}
                        title={t("registerPage.phoneNumber")}
                        value={account.phoneNumber}
                        validationType="VALIDATE_PHONENUMBER_CLIENT"
                        isValid={isPhoneNumberValidClient}
                        onChange={(e: any) =>
                            setAccount({
                                ...account,
                                phoneNumber: e.target.value,
                            })
                        }
                    />

                    <div className={styles.edit_field_validation}>
                        <ValidationMessage
                            isValid={isPhoneNumberValidClient}
                            message={t("registerPage.phoneNumberMsg")}
                        />
                    </div>
                </div>
                <div className={styles.edit_field} title="password">
                    <InputWithValidation
                        required
                        styleWidth={{ width: "20rem" }}
                        title={t("registerPage.password")}
                        type="password"
                        value={account.password}
                        validationType="VALIDATE_PASSWORD"
                        isValid={isPasswordValid}
                        onChange={(e) => {
                            setAccount({
                                ...account,
                                password: e.target.value,
                            });
                        }}
                    />

                    <div className={styles.edit_field_validation}>
                        <ValidationMessage
                            isValid={isPasswordValid}
                            message={t("registerPage.passwordMsg")}
                        />
                    </div>
                </div>
                <div className={styles.edit_field} title="repeat-password">
                    <InputWithValidation
                        required
                        styleWidth={{ width: "20rem" }}
                        title={t("registerPage.repaet_password")}
                        type="password"
                        value={repeatPassword}
                        validationType="REPEAT_PASSWORD"
                        isValid={repeatPasswordMatch}
                        onChange={(e) => {
                            setRepeatPassword(e.target.value);
                        }}
                    />

                    <div className={styles.edit_field_validation}>
                        <ValidationMessage
                            isValid={repeatPasswordMatch}
                            message={t("registerPage.repaet_passwordMsg")}
                        />
                    </div>
                </div>
                <div
                    className={styles.edit_field}
                    title={t("registerPage.language")}
                    style={{ display: "block" }}
                >
                    <p className={styles.p}>
                        {t("registerPage.language")}
                        <span style={{ color: "red" }}>*</span>
                    </p>
                    <select
                        id="languageToCreate"
                        name="languageToCreate"
                        required
                        className={styles.select}
                        onChange={(e: any) =>
                            setAccount({
                                ...account,
                                language: { language: e.target.value },
                            })
                        }
                    >
                        <option className={styles.option} value="pl" id="pl">
                            {t("registerPage.polish")}
                        </option>
                        <option className={styles.option} value="en" id="en">
                            {t("registerPage.english")}
                        </option>
                    </select>
                </div>
                <div className={styles.create_user_buttons_wrapper}>
                    <ActionButton
                        onClick={() => {
                            handleSubmit();
                        }}
                        isDisabled={!isEveryFieldValid}
                        icon={faCheck}
                        color="green"
                        title={t("registerPage.sign_in")}
                    />
                    <ActionButton
                        onClick={() => {
                            navigate("/");
                        }}
                        icon={faCancel}
                        color="red"
                        title={t("registerPage.cancel")}
                    />
                </div>
                <RegisterModal
                    isOpened={opened}
                    onClose={() => {
                        navigate("/login");
                    }}
                >
                    {t("registerPage.send_email")}
                </RegisterModal>
            </div>{" "}
        </section>
    );
};

export const RegisterPage = () => {
    return (
        <GoogleReCaptchaProvider reCaptchaKey="6Lf85hEgAAAAAONrGfo8SoQSb9GmfzHXTTgjKJzT">
            <RegisterPageInternal />
        </GoogleReCaptchaProvider>
    );
};

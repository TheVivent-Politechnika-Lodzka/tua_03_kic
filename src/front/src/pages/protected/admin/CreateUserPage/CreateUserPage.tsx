import style from "./style.module.scss";
import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useContext, useState } from "react";
import { useNavigate } from "react-router";
import { createAccount, CreateAccountRequest } from "../../../../api";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../../context/validationContext";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";
import { useTranslation } from "react-i18next";

const CreateUserPage = () => {
    const [account, setAccount] = useState<CreateAccountRequest>({
        email: "",
        firstName: "",
        lastName: "",
        login: "",
        password: "",
        language: {
            language: "pl",
        },
    });
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });

    const { t } = useTranslation();

    const [error, setError] = useState<ApiError>();
    const [opened, setOpened] = useState<boolean>(false);

    const {
        state,
        state: {
            isFirstNameValid,
            isLastNameValid,
            isEmailValidAdministrator,
            isLoginValid,
            isPasswordValid,
        },
        dispatch,
    } = useContext(validationContext);

    const navigate = useNavigate();

    const isEveryFieldValid =
        isFirstNameValid &&
        isLastNameValid &&
        isEmailValidAdministrator &&
        isLoginValid &&
        isPasswordValid;

    const handleSubmit = async () => {
        setLoading({ ...loading, actionLoading: true });

        const request = {
            ...account,
            url: "https://i1.sndcdn.com/artworks-OIPArbJVhHXzsGol-TmbtyQ-t500x500.jpg",
        };

        const response = await createAccount(request);
        if ("errorMessage" in response) {
            setError(response);
            setLoading({ ...loading, actionLoading: false });
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        navigate("/accounts");
        setLoading({ ...loading, actionLoading: false });
        showNotification(
            successNotficiationItems(
                t("createUserPage.successNotficiationItems")
            )
        );
    };

    return (
        <section className={style.create_user_page}>
            <div className={style.create_user_page_header}>
                <h2>{t("createUserPage.header")}</h2>
            </div>
            <div className={style.create_user_page_content}>
                <div className={style.create_data_account_wrapper}>
                    <div className={style.edit_fields_wrapper}>
                        <div
                            className={style.edit_field}
                            title={t("createUserPage.email")}
                        >
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title={t("createUserPage.email")}
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

                            <div className={style.edit_field_validation}>
                                <ValidationMessage
                                    isValid={isEmailValidAdministrator}
                                    message={t("createUserPage.emailMsg")}
                                />
                            </div>
                        </div>
                        <div
                            className={style.edit_field}
                            title={t("createUserPage.nameTitle")}
                        >
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title={t("createUserPage.name")}
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
                            <div className={style.edit_field_validation}>
                                <ValidationMessage
                                    isValid={isFirstNameValid}
                                    message={t("createUserPage.nameMsg")}
                                />
                            </div>
                        </div>
                        <div
                            className={style.edit_field}
                            title={t("createUserPage.surnameTitle")}
                        >
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title={t("createUserPage.surname")}
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

                            <div className={style.edit_field_validation}>
                                <ValidationMessage
                                    isValid={isLastNameValid}
                                    message={t("createUserPage.surnameMsg")}
                                />
                            </div>
                        </div>
                        <div
                            className={style.edit_field}
                            title={t("createUserPage.idTitle")}
                        >
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title={t("createUserPage.id")}
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

                            <div className={style.edit_field_validation}>
                                <ValidationMessage
                                    isValid={isLoginValid}
                                    message={t("createUserPage.idMsg")}
                                />
                            </div>
                        </div>
                        <div
                            className={style.edit_field}
                            title={t("createUserPage.passwordTitle")}
                        >
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title={t("createUserPage.password")}
                                type="password"
                                value={account.password}
                                validationType="VALIDATE_PASSWORD"
                                isValid={isPasswordValid}
                                onChange={(e) =>
                                    setAccount({
                                        ...account,
                                        password: e.target.value,
                                    })
                                }
                            />

                            <div className={style.edit_field_validation}>
                                <ValidationMessage
                                    isValid={isPasswordValid}
                                    message={t("createUserPage.passwordMsg")}
                                />
                            </div>
                        </div>
                        <div
                            className={style.edit_field}
                            title={t("createUserPage.languageTitle")}
                            style={{ display: "block" }}
                        >
                            <p className={style.p}>
                                {t("createUserPage.language")}{" "}
                                <span style={{ color: "red" }}>*</span>
                            </p>
                            <select
                                id={t("createUserPage.languageToCreate")}
                                name={t("createUserPage.languageToCreate")}
                                required
                                className={style.select}
                                onChange={(e: any) =>
                                    setAccount({
                                        ...account,
                                        language: { language: e.target.value },
                                    })
                                }
                            >
                                <option
                                    className={style.option}
                                    value="pl"
                                    id="pl"
                                >
                                    {t("createUserPage.languagePL")}
                                </option>
                                <option
                                    className={style.option}
                                    value="en"
                                    id="en"
                                >
                                    {t("createUserPage.languageEN")}
                                </option>
                            </select>
                        </div>
                    </div>
                    <div className={style.create_user_buttons_wrapper}>
                        <ActionButton
                            onClick={() => {
                                setOpened(true);
                            }}
                            isDisabled={!isEveryFieldValid}
                            icon={faCheck}
                            color="green"
                            title={t("createUserPage.accept")}
                        />
                        <ActionButton
                            onClick={() => {
                                navigate("/accounts");
                            }}
                            icon={faCancel}
                            color="red"
                            title={t("createUserPage.cancel")}
                        />
                    </div>
                </div>
            </div>

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
                title={t("createUserPage.confirmActionModal.title")}
            >
                {t("createUserPage.confirmActionModal.message")}
            </ConfirmActionModal>
        </section>
    );
};

export default CreateUserPage;

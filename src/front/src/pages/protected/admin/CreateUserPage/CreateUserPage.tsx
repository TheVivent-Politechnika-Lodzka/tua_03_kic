import style from "./style.module.scss";
import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useContext, useState } from "react";
import { useNavigate } from "react-router";
import { createAccount, CreateAccountRequest } from "../../../../api";
import ConfirmActionModal from "../../../../components/ConfirmActionModal/ConfirmActionModal";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../../context/validationContext";
import { successNotficiationItems } from "../../../../utils/showNotificationsItems";

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
        };

        const response = await createAccount(request);
        if ("errorMessage" in response) {
            setError(response);
            console.log(`${response.status} ${response.errorMessage}`);
            alert(response.errorMessage);
            setLoading({ ...loading, actionLoading: false });
            return;
        }
        navigate("/accounts");
        setLoading({ ...loading, actionLoading: false });
        showNotification(successNotficiationItems("Konto zostało utworzone"));
    };

    return (
        <section className={style.create_user_page}>
            <div className={style.create_user_page_header}>
                <h2>Utwórz nowe konto użytkownika</h2>
            </div>
            <div className={style.create_user_page_content}>
                <div className={style.create_data_account_wrapper}>
                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field} title="email">
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title="Email "
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
                                    message="Podaj poprawny adres email"
                                />
                            </div>
                        </div>
                        <div className={style.edit_field} title="firstName">
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title="Imię "
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
                                    message="Imię powinno skłądać się z minimum 3 i maksymalnie 30 znaków"
                                />
                            </div>
                        </div>
                        <div className={style.edit_field} title="lastName">
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title="Nazwisko "
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
                                    message="Nazwisko powinno skłądać się z minimum 3 i maksymalnie 30 znaków"
                                />
                            </div>
                        </div>
                        <div className={style.edit_field} title="login">
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title="Identyfikator "
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
                                    message="Identyfikator powinien skłądać się z minimum 3 i maksymalnie 30 znaków"
                                />
                            </div>
                        </div>
                        <div className={style.edit_field} title="password">
                            <InputWithValidation
                                required
                                styleWidth={{ width: "30rem" }}
                                title="Hasło "
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
                                    message="Tutaj sobie coś wpisz czy cóś"
                                />
                            </div>
                        </div>
                        <div
                            className={style.edit_field}
                            title="lenguage"
                            style={{ display: "block" }}
                        >
                            <p className={style.p}>
                                Język <span style={{ color: "red" }}>*</span>
                            </p>
                            <select
                                id="lenguageToCreate"
                                name="lenguageToCreate"
                                // value={lenguageFromForm}
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
                                    polski
                                </option>
                                <option
                                    className={style.option}
                                    value="en"
                                    id="en"
                                >
                                    angielski
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
                            title="Zatwierdź"
                        />
                        <ActionButton
                            onClick={() => {
                                navigate("/accounts");
                            }}
                            icon={faCancel}
                            color="red"
                            title="Anuluj"
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
                title="Utwórz nowego użytkownika"
            >
                Czy na pewno chcesz utworzyć nowego użytkownika? Operacja jest
                nieodwracalna
            </ConfirmActionModal>
        </section>
    );
};

export default CreateUserPage;

import { useContext, useEffect, useState } from "react";
import avatar from "../../../../assets/images/avatar.jpg";
import "./style.scss";
import { faCancel, faCheck, faEdit } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useNavigate } from "react-router-dom";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import { validationContext } from "../../../../context/validationContext";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import {
    editOwnAccount,
    GetAccountResponse,
    getOwnAccount,
} from "../../../../api";
import ReactLoading from "react-loading";

const EditOwnAccountPage = () => {
    const [account, setAccount] = useState<GetAccountResponse>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [error, setError] = useState<ApiError>();

    const accessLevel = useStoreSelector((state) => state.user.cur);

    const {
        state,
        state: {
            isFirstNameValid,
            isLastNameValid,
            isPhoneNumberValid,
            isPESELValid,
            isEmailValid,
        },
        dispatch,
    } = useContext(validationContext);

    const navigate = useNavigate();

    const handleGetOwnAccount = async () => {
        try {
            const data = await getOwnAccount();
            if ("errorMessage" in data) return;
            setAccount(data);
            setLoading({ ...loading, pageLoading: false });
        } catch (error: ApiError | any) {
            setLoading({ ...loading, pageLoading: false });
            setError(error);
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    const handleSubmit = async () => {
        if (!account) return;
        setLoading({ ...loading, actionLoading: true });
        const request = {
            ...account,
            captcha: "xd",
        };
        const response = await editOwnAccount(request);
        if ("errorMessage" in response) {
            setError(response);
            console.error(`${response.status} ${response.errorMessage}`);
            alert(response.errorMessage);
            setLoading({ ...loading, actionLoading: false });
            return;
        }
        setAccount(response);
        setLoading({ ...loading, actionLoading: false });
    };

    useEffect(() => {
        // Gdyby uzywac przy rejestracji to trzeba dodac nową akcję w reducerze albo idk nowy initialState
        dispatch({ type: "RESET_VALIDATION", payload: { ...state } });
        handleGetOwnAccount();
    }, []);

    const isEveryFieldValid =
        isFirstNameValid &&
        isLastNameValid &&
        isPhoneNumberValid &&
        isPESELValid &&
        isEmailValid;

    return (
        <section className="edit-own-account-page">
            {loading.pageLoading ? (
                <ReactLoading
                    type="cylon"
                    color="#fff"
                    width="10rem"
                    height="10rem"
                />
            ) : (
                <>
                    <div className="edit-own-account-header">
                        <h2>Edytuj dane własnego konta:</h2>
                    </div>
                    <div className="edit-own-account-content">
                        <div className="edit-data-account-wrapper">
                            <div className="avatar-wrapper">
                                <img
                                    src={avatar}
                                    alt="User avatar"
                                    className="change-avatar"
                                />
                                <div className="edit-avatar-icon-wrapper">
                                    <FontAwesomeIcon
                                        icon={faEdit}
                                        className="edit-avatar-icon"
                                    />
                                </div>
                            </div>
                            <div className="edit-fields-wrapper">
                                <div className="edit-field">
                                    <InputWithValidation
                                        title="Imię: "
                                        value={account?.firstName}
                                        validationType="VALIDATE_FIRSTNAME"
                                        isValid={isFirstNameValid}
                                        onChange={(e) => {
                                            if (e.target.value && account)
                                                setAccount({
                                                    ...account,
                                                    firstName: e.target.value,
                                                });
                                        }}
                                    />
                                    <ValidationMessage
                                        isValid={isFirstNameValid}
                                        message="Imię musi składać się z co najmniej 3 liter."
                                    />
                                </div>
                                <div className="edit-field">
                                    <InputWithValidation
                                        title="Nazwisko: "
                                        value={account?.lastName}
                                        validationType="VALIDATE_LASTNAME"
                                        isValid={isLastNameValid}
                                        onChange={(e) => {
                                            if (e.target.value && account)
                                                setAccount({
                                                    ...account,
                                                    lastName: e.target.value,
                                                });
                                        }}
                                    />
                                    <ValidationMessage
                                        isValid={isLastNameValid}
                                        message="Nazwisko musi składać się z co najmniej 3 liter."
                                    />
                                </div>

                                {accessLevel === "CLIENT" ? (
                                    <>
                                        <div className="edit-field">
                                            <InputWithValidation
                                                title="Numer PESEL: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                "CLIENT"
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.pesel
                                                        )[0]
                                                }
                                                validationType="VALIDATE_PESEL"
                                                isValid={isPESELValid}
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        "CLIENT"
                                                                    ) {
                                                                        level.pesel =
                                                                            e.target.value;
                                                                    }
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={isPESELValid}
                                                message="Numer pesel musi składać się z 11 cyfr."
                                            />
                                        </div>
                                        <div className="edit-field">
                                            <InputWithValidation
                                                title="Numer telefonu: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                accessLevel
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.phoneNumber
                                                        )[0]
                                                }
                                                validationType="VALIDATE_PHONENUMBER"
                                                isValid={isPhoneNumberValid}
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        accessLevel
                                                                    )
                                                                        level.phoneNumber =
                                                                            e.target.value;
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={isPhoneNumberValid}
                                                message="Numer telefonu musi składać się z 9 cyfr."
                                            />
                                        </div>
                                    </>
                                ) : null}

                                {["ADMINISTRATOR", "SPECIALIST"].includes(
                                    accessLevel
                                ) ? (
                                    <>
                                        <div className="edit-field">
                                            <InputWithValidation
                                                title="Numer telefonu: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                accessLevel
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.phoneNumber
                                                        )[0]
                                                }
                                                validationType="VALIDATE_PHONENUMBER"
                                                isValid={isPhoneNumberValid}
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        accessLevel
                                                                    )
                                                                        level.phoneNumber =
                                                                            e.target.value;
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={isPhoneNumberValid}
                                                message="Numer telefonu musi składać się z 9 cyfr."
                                            />
                                        </div>
                                        <div className="edit-field">
                                            <InputWithValidation
                                                title="Email kontaktowy: "
                                                value={
                                                    account?.accessLevels
                                                        .filter(
                                                            (level) =>
                                                                level.level ===
                                                                accessLevel
                                                        )
                                                        .map(
                                                            (level) =>
                                                                level.contactEmail
                                                        )[0]
                                                }
                                                validationType="VALIDATE_EMAIL"
                                                isValid={isEmailValid}
                                                onChange={(e) => {
                                                    if (
                                                        e.target.value &&
                                                        account
                                                    )
                                                        setAccount((old) => {
                                                            if (!old) return;
                                                            old.accessLevels.forEach(
                                                                (level) => {
                                                                    if (
                                                                        level.level ===
                                                                        accessLevel
                                                                    )
                                                                        level.contactEmail =
                                                                            e.target.value;
                                                                }
                                                            );
                                                            return old;
                                                        });
                                                }}
                                            />
                                            <ValidationMessage
                                                isValid={isEmailValid}
                                                message="Email musi być poprawny."
                                            />
                                        </div>
                                    </>
                                ) : null}
                            </div>
                            <div className="edit-data-buttons-wrapper">
                                <ActionButton
                                    onClick={handleSubmit}
                                    isDisabled={!isEveryFieldValid}
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
                    </div>
                </>
            )}
        </section>
    );
};

export default EditOwnAccountPage;

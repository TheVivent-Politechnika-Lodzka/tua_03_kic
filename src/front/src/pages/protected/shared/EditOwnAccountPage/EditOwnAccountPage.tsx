import { useContext, useEffect, useState } from "react";
import avatar from "../../../../assets/images/avatar.jpg";
import "./style.scss";
import { faCancel, faCheck, faEdit } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useNavigate } from "react-router-dom";
import InputWithValidation from "../../../../component/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../component/shared/ValidationMessage/ValidationMessage";
import ActionButton from "../../../../component/shared/ActionButton/ActionButton";
import { validationContext } from "../../../../context/validationContext";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import { getOwnAccount } from "../../../../api";
import ReactLoading from "react-loading";

const EditOwnAccountPage = () => {
    const [account, setAccount] = useState<AccountDetails>();
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
        },
        dispatch,
    } = useContext(validationContext);

    const navigate = useNavigate();

    const handleGetOwnAccount = async () => {
        try {
            const data = await getOwnAccount();
            console.log(data);
            setAccount(data as AccountDetails);
            setLoading({ ...loading, pageLoading: false });
        } catch (error: ApiError | any) {
            setLoading({ ...loading, pageLoading: false });
            setError(error);
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
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
        isPESELValid;

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
                                    />
                                    <ValidationMessage
                                        isValid={isLastNameValid}
                                        message="Nazwisko musi składać się z co najmniej 3 liter."
                                    />
                                </div>

                                <div className="edit-field">
                                    {accessLevel === "CLIENT" ? (
                                        <>
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
                                            />
                                            <ValidationMessage
                                                isValid={isPESELValid}
                                                message="Numer pesel musi składać się z 11 cyfr."
                                            />
                                        </>
                                    ) : (
                                        <>
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
                                            />
                                            <ValidationMessage
                                                isValid={isPhoneNumberValid}
                                                message="Numer telefonu musi składać się z 9 cyfr."
                                            />
                                        </>
                                    )}
                                </div>
                            </div>
                            <div className="edit-data-buttons-wrapper">
                                <ActionButton
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

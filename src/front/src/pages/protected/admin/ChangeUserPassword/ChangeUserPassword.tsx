import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { useContext, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate, useParams } from "react-router";
import {
    useChangeAccountPasswordMutation,
    useGetAccountByLoginMutation,
} from "../../../../api/api";
import { ChangePasswordDto } from "../../../../api/types/apiParams";
import ConfirmActionModal from "../../../../components/ConfirmActionModal/ConfirmActionModal";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../../context/validationContext";
import styles from "./style.module.scss";

const ChangeUserPassword = () => {
    const navigate = useNavigate();
    const { login } = useParams();
    const [newPassword, setNewPassword] = useState<string>("");
    const [opened, setOpened] = useState<boolean>(false);
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [accountData, setAccountData] = useState<ChangePasswordDto>({
        login: login as string,
        data: {
            ETag: "",
            newPassword: "",
        },
    });
    const [getAccount] = useGetAccountByLoginMutation();
    const [changePassword] = useChangeAccountPasswordMutation();
    const {
        state: { isNewPasswordValid },
    } = useContext(validationContext);
    const { t } = useTranslation();

    const getEtag = async () => {
        const result = await getAccount(login as string);
        if ("data" in result) {
            setAccountData({
                ...accountData,
                data: {
                    ...accountData.data,
                    ETag: result?.data?.ETag,
                },
            });
        }
    };

    useEffect(() => {
        getEtag();
    }, []);

    return (
        <div className={styles.change_user_password_page}>
            <div className={styles.title_wrapper}>
                <h2>
                    {t("ChangeAccountPassword.title")} {login}
                </h2>
            </div>
            <div className={styles.content}>
                <div className={styles.change_password_wrapper}>
                    <InputWithValidation
                        title="Nowe hasło: "
                        validationType="VALIDATE_NEW_PASSWORD"
                        isValid={isNewPasswordValid}
                        value={newPassword}
                        type="password"
                        onChange={(e) => setNewPassword(e.target.value)}
                    />
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
            </div>
            <div className={styles.validation_status_wrapper}>
                <ValidationMessage
                    isValid={isNewPasswordValid}
                    message="Hasło musi być dłuższe niż 8 znaków oraz musi zawierać co jedną dużą literę, jedną cyfrę i jeden znak specjalny"
                />
            </div>
            <ConfirmActionModal
                isOpened={opened}
                onClose={() => {
                    setOpened(false);
                }}
                handleFunction={async () => {
                    // await handleSubmit();
                    setOpened(false);
                }}
                isLoading={loading.actionLoading as boolean}
                title={`Zmiana hasła użytkownika`}
                description={`Czy na pewno chcesz zmienić hasło użytkownika ${login}? Operacja jest nieodwracalna`}
            />
        </div>
    );
};

export default ChangeUserPassword;

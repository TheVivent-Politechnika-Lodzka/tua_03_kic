import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router";
import styles from "./userDetails.module.scss";
import { AccountWithAccessLevelsDto } from "../../../api/types/apiParams";
import { useGetAccountByLoginMutation } from "../../../api/api";
import { useTranslation } from "react-i18next";
import AddAccessLevelButton from "../../../component/Button/AddAccessLevelButton/AddAccessLevelButton";
import RemoveAccessLevelForm from "../../../component/Form/removeAccessLevelForm/RemoveAccessLevelForm";
import EditAccountDataForm from "../../../component/Form/editAccountDataForm/EditAccountDataForm";

const UserDetails = () => {
    const [user, setUser] = useState<AccountWithAccessLevelsDto>();
    const [getUser] = useGetAccountByLoginMutation();
    const navigate = useNavigate();
    const { login } = useParams();
    const { t } = useTranslation();

    const handleGetUser = async () => {
        const user = await getUser(login as string);
        if ("data" in user) {
            setUser(user.data);
        }
    };

    useEffect(() => {
        handleGetUser();
    }, []);

    return (
        <div className={styles.userDetails}>
            <h2>Dane użytkownika: {login}</h2>
            {user ? (
                <div className={styles.pageLayout}>
                    <p>
                        {t("header_login")}: {user.login}
                    </p>
                    <p>
                        {t("header_first_name")}: {user.firstName}
                    </p>
                    <p>
                        {t("header_last_name")}: {user.lastName}
                    </p>
                    <p>
                        {t("header_email")}: {user.email}
                    </p>
                    <p>
                        {t("header_is_active")}:{" "}
                        {user.active ? t("cell_active") : t("cell_inactive")}
                    </p>
                    <p>
                        {t("header_is_confirmed")}:{" "}
                        {user.active
                            ? t("cell_confirmed")
                            : t("cell_unconfirmed")}
                    </p>
                    <p>
                        {t("header_language")}: {user.language?.language}
                    </p>
                    <p>
                        {t("header_created_at")}: {user.createdAt}
                    </p>
                    <p>
                        {t("header_last_modified")}: {user.lastModified}
                    </p>
                    <p>
                        {t("header_level_access_level")}:
                        {user.accessLevels.map(
                            ({
                                level,
                                pesel,
                                phoneNumber,
                                contactEmail,
                                ETag,
                            }) => (
                                <div>
                                    <span>
                                        {level} {pesel} {phoneNumber}{" "}
                                        {contactEmail}
                                    </span>
                                    <RemoveAccessLevelForm
                                        login={login || ""}
                                        eTag={ETag || ""}
                                        level={level}
                                        callback={setUser}
                                    />
                                </div>
                            )
                        )}
                    </p>
                </div>
            ) : (
                <p>Nie znaleziono uzytkownika {login} Sadeg</p>
            )}
            <button
                onClick={() => {
                    navigate("/admin/users");
                }}
                className={styles.button}
            >
                powrót
            </button>
            <AddAccessLevelButton login={login || ""} callback={setUser} />
            <div></div>
            <div>
                <EditAccountDataForm login={login || ""} callback={setUser} />
            </div>
        </div>
    );
};

export default UserDetails;

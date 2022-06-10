import { ChangeEvent, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router";
import { useFindAllUsersMutation } from "../../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../../api/types/apiParams";
import { ShowAccountInfo } from "../../../../api/types/common";
import AccountDetails from "../../../../component/AccountDetails/AccountDetails";
import ActivateButton from "../../../../component/Button/ActivateButton/ActivateButton";
import { DeactivateButton } from "../../../../component/Button/DeactivateButton/DeactivateButton";
import styles from "./userManagment.module.scss";

const UserManagment = () => {
    const [account, setAccounts] = useState<ShowAccountInfo[]>([]);
    const [modal, setModal] = useState<boolean>(false);
    const [userLogin, setUserLogin] = useState<string>("");
    const [query, setQuery] = useState<string>("");
    const [findAll] = useFindAllUsersMutation();
    const navigate = useNavigate();
    const { t } = useTranslation();
    const [page, setPage] = useState<number>(1);
    const limit = 3;

    useEffect(() => {
        getAccounts();
    }, [page]);

    useEffect(() => {
        const timer = setTimeout(() => {
            getAccounts();
        }, 500);
        return () => clearTimeout(timer);
    }, [query]);

    const getAccounts = async () => {
        const data = await findAll({ page: page, limit: limit, phrase: query });
        let accounts;
        if ("data" in data) {
            // wygląda paskudnie, ale na razie zostawmy
            accounts = data.data?.data?.map(
                ({
                    login,
                    accessLevels,
                    email,
                    active,
                    firstName,
                    lastName,
                    ETag,
                }: AccountWithAccessLevelsDto): ShowAccountInfo => {
                    return {
                        firstName: firstName,
                        lastName: lastName,
                        login: login,
                        accessLevels: accessLevels.map(({ level }) => {
                            return `${level}\n`;
                        }),
                        email: email,
                        active: active,
                        ETag: ETag,
                    };
                }
            );
            setAccounts(accounts);
        }
    };

    const handleQuery = (e: ChangeEvent<HTMLInputElement>) => {
        setQuery(e.target.value);
    };

    return (
        <div className={styles.pageLayout}>
            <h2>{t("users_administration_page")}</h2>
            <input
                type="text"
                className={styles.searchInput}
                placeholder={t("search_by_phrase_placeholder")}
                onChange={handleQuery}
                value={query}
            />
            <table className={styles.table}>
                <thead>
                    <tr>
                        <th>{t("header_login")}</th>
                        <th>{t("header_first_name")}</th>
                        <th>{t("header_last_name")}</th>
                        <th>{t("header_level_access_level")}</th>
                        <th>{t("header_email")}</th>
                        <th>{t("header_is_active")}</th>
                        <th>{t("header_account_details")}</th>
                        <th>{t("header_account_details")}</th>
                        <th>{t("header_manage_activity")}</th>
                    </tr>
                </thead>
                <tbody>
                    {account?.map(
                        ({
                            login,
                            firstName,
                            lastName,
                            accessLevels,
                            email,
                            active,
                            ETag,
                        }) => {
                            return (
                                <tr key={email}>
                                    <td>{login}</td>
                                    <td>{firstName}</td>
                                    <td>{lastName}</td>
                                    <td>{accessLevels}</td>
                                    <td>{email}</td>
                                    <td>
                                        {active
                                            ? t("cell_active")
                                            : t("cell_inactive")}
                                    </td>
                                    <td>
                                        <button
                                            onClick={() => {
                                                setUserLogin(login);
                                                setModal(true);
                                            }}
                                            className={styles.button}
                                        >
                                            Details (ale te nowe)
                                        </button>
                                    </td>
                                    <td>
                                        <button
                                            onClick={() => {
                                                navigate(`/accounts/${login}`);
                                            }}
                                            className={styles.button}
                                        >
                                            {t("header_account_details")}
                                        </button>
                                    </td>
                                    <td>
                                        {active ? (
                                            <DeactivateButton
                                                login={login}
                                                ETag={ETag}
                                            ></DeactivateButton>
                                        ) : (
                                            <ActivateButton
                                                login={login}
                                                ETag={ETag}
                                            ></ActivateButton>
                                        )}
                                    </td>
                                </tr>
                            );
                        }
                    )}
                </tbody>
            </table>
            <div>
                <button
                    className={styles.button}
                    onClick={() =>
                        setPage((old) => {
                            return old === 1 ? 1 : old - 1;
                        })
                    }
                >
                    {"<"}
                </button>
                <span>{page}</span>
                <button
                    className={styles.button}
                    onClick={() => setPage((old) => old + 1)}
                >
                    {">"}
                </button>
            </div>
            <AccountDetails
                onClose={() => setModal(false)}
                login={userLogin}
                isOpened={modal}
            />
        </div>
    );
};

export default UserManagment;

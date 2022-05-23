import { ChangeEvent, useEffect, useState } from "react";
import { Modal } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import { Navigate, useNavigate } from "react-router";
import { useFindAllUsersMutation } from "../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../api/types/apiParams";
import { ShowAccountInfo } from "../../../api/types/common";
import styles from "./userManagment.module.scss";

const UserManagment = () => {
  const [account, setAccounts] = useState<ShowAccountInfo[]>([]);
  const [query, setQuery] = useState<string>("");
  const [findAll] = useFindAllUsersMutation();
  const navigate = useNavigate();
  const { t } = useTranslation();

  useEffect(() => {
    getAccounts();
  }, []);

  useEffect(() => {
    const timer = setTimeout(() => {
      getAccounts();
    }, 500);
    return () => clearTimeout(timer);
  }, [query]);

  const getAccounts = async () => {
    const data = await findAll({ page: 1, limit: 3, phrase: query });
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
            <th>{t("header_change_password")}</th>
            <th>{t("header_account_details")}</th>
          </tr>
        </thead>
        <tbody>
          {account?.map(
            ({ login, firstName, lastName, accessLevels, email, active }) => {
              return (
                <tr key={email}>
                  <td>{login}</td>
                  <td>{firstName}</td>
                  <td>{lastName}</td>
                  <td>{accessLevels}</td>
                  <td>{email}</td>
                  <td>{active ? t("cell_active") : t("cell_inactive")}</td>
                  <td>
                    <button className={styles.button}>
                      {t("header_change_password")}
                    </button>
                  </td>
                  <td>
                    <button
                      onClick={() => {
                        navigate(`/admin/users/${login}`);
                      }}
                      className={styles.button}
                    >
                      {t("header_account_details")}
                    </button>
                  </td>
                </tr>
              );
            }
          )}
        </tbody>
      </table>
    </div>
  );
};

export default UserManagment;

import { ChangeEvent, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router";
import { useFindAllUsersMutation } from "../../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../../api/types/apiParams";
import { ShowAccountInfo } from "../../../../api/types/common";
import AccountDetails from "../../../../components/AccountDetails/AccountDetails";
import ActivateButton from "../../../../components/Button/ActivateButton/ActivateButton";
import { DeactivateButton } from "../../../../components/Button/DeactivateButton/DeactivateButton";
import SearchInput from "../../../../components/SearchInput/SearchInput";
import UserRecord from "../../../../components/UserRecord/UserRecord";
import styles from "./style.module.scss";

const UsersManagmentPage = () => {
    const navigate = useNavigate();
    const { t } = useTranslation();

    return (
        <section className={styles.users_managment_page}>
            <div className={styles.input_container}>
                <SearchInput placeholder="Wpisz frazę..." />
            </div>
            <div className={styles.table_container}>
                <div className={styles.table_header}>
                    <div className={styles.cell_wrapper}>
                        <p className={styles.cell}>{" "}</p>
                        <p className={styles.cell}>Login</p>
                        <p className={styles.cell}>Imię</p>
                        <p className={styles.cell}>Nazwisko</p>
                        <p className={styles.cell}>Czy zatwierdzone</p>
                        <p className={styles.cell}>Czy aktywne</p>
                        <p className={styles.cell}>Więcej</p>
                    </div>
                </div>
                <div className={styles.table_body}>
                    <UserRecord />
                    <UserRecord />
                    <UserRecord />
                </div>
            </div>
        </section>
    );
};

export default UsersManagmentPage;

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
import styles from "./style.module.scss";

const UsersManagmentPage = () => {
    const [account, setAccounts] = useState<ShowAccountInfo[]>([]);
    const [modal, setModal] = useState<boolean>(false);
    const [userLogin, setUserLogin] = useState<string>("");
    const [query, setQuery] = useState<string>("");
    const [findAll] = useFindAllUsersMutation();
    const navigate = useNavigate();
    const { t } = useTranslation();
    const [page, setPage] = useState<number>(1);
    const limit = 3;

    return (
        <section className={styles.users_managment_page}>
            <div className={styles.input_container}>
                <SearchInput placeholder="Wpisz frazę..." />
            </div>
            <div className={styles.table_container}>
                    
            </div>
        </section>
    );
};

export default UsersManagmentPage;

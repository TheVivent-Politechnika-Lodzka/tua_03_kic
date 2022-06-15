import { ChangeEvent, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router";
import { listAccounts } from "../../../../api";
import { useFindAllUsersMutation } from "../../../../api/api";
import { AccountWithAccessLevelsDto } from "../../../../api/types/apiParams";
import { ShowAccountInfo } from "../../../../api/types/common";
import ActivateButton from "../../../../components/Button/ActivateButton/ActivateButton";
import { DeactivateButton } from "../../../../components/Button/DeactivateButton/DeactivateButton";
import SearchInput from "../../../../components/SearchInput/SearchInput";
import UserRecord from "../../../../components/UserRecord/UserRecord";
import ReactLoading from "react-loading";
import styles from "./style.module.scss";

const UsersManagmentPage = () => {
    const [users, setUsers] = useState<AccountDetails[]>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [error, setError] = useState<ApiError>();
    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleGetAllUsers = async () => {
        try {
            const data = await listAccounts({ page: 1, limit: 4 });
            if ("errorMessage" in data) return;
            setUsers(data.data);
            setLoading({ ...loading, pageLoading: false });
        } catch (error: ApiError | any) {
            setLoading({ ...loading, pageLoading: false });
            setError(error);
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    useEffect(() => {
        handleGetAllUsers();
    }, []);

    return (
        <section className={styles.users_managment_page}>
            <div className={styles.input_container}>
                <SearchInput placeholder="Wpisz frazę..." />
            </div>

            <div className={styles.table_container}>
                {" "}
                {loading.pageLoading ? (
                    <ReactLoading
                        type="cylon"
                        color="#fff"
                        width="10rem"
                        height="10rem"
                        className={styles.loading}
                    />
                ) : (
                    <>
                        <div className={styles.table_header}>
                            <div className={styles.cell_wrapper}>
                                <p className={styles.cell}> </p>
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
                        </div>
                    </>
                )}
            </div>
        </section>
    );
};

export default UsersManagmentPage;

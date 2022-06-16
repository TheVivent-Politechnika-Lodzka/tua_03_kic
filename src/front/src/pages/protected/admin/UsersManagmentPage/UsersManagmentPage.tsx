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
import { retry } from "@reduxjs/toolkit/dist/query";
import Pagination from "../../../../components/Pagination/Pagination";

const UsersManagmentPage = () => {
    const [users, setUsers] = useState<AccountDetails[]>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [error, setError] = useState<ApiError>();
    const [phrase, setPhrase] = useState<string>(" ");
    const [rerender, setRerender] = useState<boolean>(false);

    const [pagination, setPagination] = useState<Pagination>({
        currentPage: 1,
        pageSize: 4,
        totalPages: 0,
    });

    const navigate = useNavigate();
    const { t } = useTranslation();

    const handleGetAllUsers = async () => {
        try {
            setLoading({ ...loading, actionLoading: true });
            const data = await listAccounts({
                page: pagination?.currentPage as number,
                limit: 4,
                phrase: phrase,
            });
            if ("errorMessage" in data) return;
            setUsers(data.data);
            setPagination({ ...pagination, totalPages: data.totalPages });
            setLoading({ pageLoading: false, actionLoading: false });
            setRerender(false);
        } catch (error: ApiError | any) {
            setLoading({ pageLoading: false, actionLoading: false });
            setError(error);
            setRerender(false);
            console.error(`${error?.status} ${error?.errorMessage}`);
        }
    };

    useEffect(() => {
        handleGetAllUsers();
    }, [rerender, pagination.currentPage]);

    useEffect(() => {
        if (phrase.length === 0) {
            handleGetAllUsers();
            return;
        }
        if (phrase.length < 3) return;
        const timer = setTimeout(() => {
            handleGetAllUsers();
        }, 500);
        return () => clearTimeout(timer);
    }, [phrase]);

    return (
        <section className={styles.users_managment_page}>
            <div className={styles.input_container}>
                <SearchInput
                    onChange={setPhrase}
                    placeholder="Wpisz frazę..."
                />
            </div>

            <div className={styles.table_container}>
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
                            {users?.map((user) => (
                                <UserRecord
                                    handleChange={() => {
                                        setRerender(true);
                                    }}
                                    key={user.id}
                                    user={user}
                                />
                            ))}
                        </div>
                        <Pagination pagination={pagination} handleFunction={setPagination}/>
                    </>
                )}
            </div>
        </section>
    );
};

export default UsersManagmentPage;

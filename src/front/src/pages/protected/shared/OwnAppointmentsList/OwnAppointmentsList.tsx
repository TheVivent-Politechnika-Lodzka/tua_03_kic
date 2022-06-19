import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import ReactLoading from "react-loading";
import { useNavigate } from "react-router";
import {
    AppointmentListElementDto,
    listOwnAppointments,
} from "../../../../api/mop";
import { AppointmentRecord } from "../../../../components/AppointmentRecord";
import Pagination from "../../../../components/Pagination/Pagination";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import styles from "./style.module.scss";

export const OwnAppointmentsList = () => {
    const aLevel = useStoreSelector((state) => state.user.cur);
    const [appointments, setAppointments] =
        useState<AppointmentListElementDto[]>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [error, setError] = useState<ApiError>();
    const [rerender, setRerender] = useState<boolean>(false);

    const [pagination, setPagination] = useState<Pagination>({
        currentPage: 1,
        pageSize: 7,
        totalPages: 0
    });
    
    useEffect(() => {
        handleGetOwnAppointments();
    }, [rerender, pagination.currentPage]);

    const { t } = useTranslation();

    const handleGetOwnAppointments = async () => {
        try {
            setLoading({ ...loading, actionLoading: true });
            const data = await listOwnAppointments({
                page: pagination?.currentPage as number,
                size: pagination?.pageSize as number,
            });
            if ("errorMessage" in data) return;
            setAppointments(data.data);
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
    return (
        <section className={styles.appointment_managment_page}>
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
                                <p className={styles.cell}>{(aLevel === "SPECIALIST") ? 'Imię i nazwisko klienta' : 'Imie i nazwisko specjalisty' }</p>
                                <p className={styles.cell}>Nazwa implantu</p>
                                <p className={styles.cell}>Status wizyty</p>
                                <p className={styles.cell}>Data wizyty</p>
                                <p className={styles.cell}>Szczegóły</p>
                            </div>
                        </div>
                        <div className={styles.table_body}>
                            {appointments?.map((appointment) => (
                                <AppointmentRecord
                                appointment={appointment}
                                key={appointment?.id}
                                />
                            ))}
                        </div>
                        <Pagination
                            pagination={pagination}
                            handleFunction={setPagination}
                        />
                    </>
                )}
            </div>
        </section>
    );
};

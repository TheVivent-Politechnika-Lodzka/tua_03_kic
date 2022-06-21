import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import ReactLoading from "react-loading";
import {
    AppointmentListElementDto,
    listOwnAppointments,
} from "../../../../api/mop";
import { AppointmentRecord } from "../../../../components/AppointmentRecord";
import Pagination from "../../../../components/Pagination/Pagination";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import { failureNotificationItems } from "../../../../utils/showNotificationsItems";
import styles from "./style.module.scss";

const OwnAppointmentsListPage = () => {
    const aLevel = useStoreSelector((state) => state.user.cur);
    const [appointments, setAppointments] =
        useState<AppointmentListElementDto[]>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [rerender, setRerender] = useState<boolean>(false);

    const [pagination, setPagination] = useState<Pagination>({
        currentPage: 1,
        pageSize: 7,
        totalPages: 0,
    });

    useEffect(() => {
        handleGetOwnAppointments();
    }, [rerender, pagination.currentPage]);

    const { t } = useTranslation();

    const handleGetOwnAppointments = async () => {
        setLoading({ ...loading, actionLoading: true });
        const data = await listOwnAppointments({
            page: pagination?.currentPage as number,
            size: pagination?.pageSize as number,
        });
        if ("errorMessage" in data) {
            showNotification(failureNotificationItems(data.errorMessage));
            return;
        }
        setAppointments(data.data);
        setPagination({ ...pagination, totalPages: data.totalPages });
        setLoading({ pageLoading: false, actionLoading: false });
        setRerender(false);
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
                                <p className={styles.cell}>
                                    {aLevel === "SPECIALIST"
                                        ? t(
                                              "ownAppointmentListPage.cell.clientName"
                                          )
                                        : t(
                                              "ownAppointmentListPage.cell.specialistName"
                                          )}
                                </p>
                                <p className={styles.cell}>
                                    {t("ownAppointmentListPage.cell.name")}
                                </p>
                                <p className={styles.cell}>
                                    {t("ownAppointmentListPage.cell.status")}
                                </p>
                                <p className={styles.cell}>
                                    {t("ownAppointmentListPage.cell.date")}
                                </p>
                                <p className={styles.cell}>
                                    {t("ownAppointmentListPage.cell.details")}
                                </p>
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

export default OwnAppointmentsListPage;

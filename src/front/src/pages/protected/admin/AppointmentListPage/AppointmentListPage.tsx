import { showNotification } from "@mantine/notifications";
import { Input} from "@mantine/core";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import ReactLoading from "react-loading";
import { listAppointments, listMyAppointments } from "../../../../api";
import { AppointmentRecord } from "../../../../components/AppointmentRecord";
import Pagination from "../../../../components/Pagination/Pagination";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import { failureNotificationItems } from "../../../../utils/showNotificationsItems";
import styles from "./style.module.scss";

export const AppointmentListPage = () => {
    const [phrase, setPhrase] = useState<string>("");
    const [appointments, setAppointments] =
        useState<AppointmentListElementDto[]>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [rerender, setRerender] = useState<boolean>(false);

    const [pagination, setPagination] = useState<Pagination>({
        currentPage: 1,
        pageSize: 6,
        totalPages: 0,
    });

    const { t } = useTranslation();
    const handleGetAppointments = async () => {
        setLoading({ ...loading, actionLoading: true });
        const data = await listAppointments({
            page: pagination?.currentPage as number,
            size: pagination?.pageSize as number,
            phrase: phrase,
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
    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            handleGetAppointments();
        }, 600);

        return () => clearTimeout(delayDebounceFn);
    }, [phrase]);

    useEffect(() => {
        handleGetAppointments();
    }, [pagination.currentPage]);

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
                    <Input
                        className="search"
                        // icon={<FaSearch size={"26px"} />}
                        placeholder={t("implantListPage.search")}
                        value={phrase}
                        onChange={(e: any) => setPhrase(e.target.value)}
                        styles={{
                            defaultVariant: {
                                backgroundColor: "#262633",
                                color: "#737373",
                                fontSize: "2rem",
                                fontWeight: "bold",
                                marginLeft: "1rem",
                            },
                            input: {
                                color: "#737373",
                                fontSize: "2rem",
                                height: "80%",
                                borderRadius: "1rem",
                            },
                            icon: {
                                marginLeft: "1rem",
                                height: "80%",
                                zIndex: 0,
                            },
                            wrapper: { height: "100%", padding: "2rem" },
                        }}
                    />
                        <div className={styles.table_header}>
                            <div className={styles.cell_wrapper}>
                                <p className={styles.cell}>
                                    {t(
                                              "ownAppointmentListPage.cell.specialistLogin"
                                          )}
                                </p>
                                <p className={styles.cell}>
                                    {t(
                                        "ownAppointmentListPage.cell.clientLogin"
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

export default AppointmentListPage;

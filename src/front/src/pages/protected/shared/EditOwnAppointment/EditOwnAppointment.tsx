import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { ChronoUnit, Instant, LocalDateTime } from "@js-joda/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import ReactLoading from "react-loading";
import { useNavigate, useParams } from "react-router";
import {
    AppointmentListElementDto,
    getAppointmentDetails,
} from "../../../../api/mop";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import { failureNotificationItems } from "../../../../utils/showNotificationsItems";
import styles from "./style.module.scss";

export const EditOwnAppointment = () => {
    const aLevel = useStoreSelector((state) => state.user.cur);
    const [appointment, setAppointment] = useState<AppointmentListElementDto>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [startDate, setStartDate] = useState<string>("");
    const [endDate, setEndDate] = useState<string>("");
    let { id } = useParams();
    const navigate = useNavigate();

    const handleGetAppointmentDetails = async () => {
        if (!id) return;
        const data = await getAppointmentDetails(id);
        if ("errorMessage" in data) {
            showNotification(failureNotificationItems(data.errorMessage));
            return;
        }
        setAppointment(data.data);
        setLoading({ pageLoading: false, actionLoading: false });
    };
    useEffect(() => {
        handleGetAppointmentDetails();
    }, []);
    useEffect(() => {
        if (!appointment) return;
        const startDate = Instant.parse(appointment.startDate);
        const fixedStartDate = LocalDateTime.ofInstant(startDate);
        const endDate = Instant.parse(appointment.endDate);
        const fixedEndDate = LocalDateTime.ofInstant(endDate);
        setStartDate(
            fixedStartDate
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()
                .replace("T", " ")
        );
        setEndDate(
            fixedEndDate
                .truncatedTo(ChronoUnit.MINUTES)
                .toString()
                .replace("T", " ")
        );
    }, [appointment]);
    return (
        <div className={styles.edit_appointment_page}>
            {loading.pageLoading ? (
                <ReactLoading
                    type="cylon"
                    color="#fff"
                    width="10rem"
                    height="10rem"
                    className={styles.loading}
                />
            ) : (
                <div className={styles.account_details}>
                    <p className={styles.account_details_title}>
                        Szczegóły wizyty
                    </p>
                    <div className={styles.details_wrapper}>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {aLevel === "SPECIALIST"
                                    ? "Imię i nazwisko klienta:"
                                    : "Imie i nazwisko specjalisty:"}
                            </p>
                            <p className={styles.description}>
                                {aLevel === "SPECIALIST"
                                    ? appointment?.client.firstName +
                                      " " +
                                      appointment?.client.lastName
                                    : appointment?.specialist.firstName +
                                      " " +
                                      appointment?.specialist.lastName}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>Email kontaktowy:</p>
                            <p className={styles.description}>
                                {appointment?.specialist.email}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>Opis:</p>
                            <p className={styles.description}>
                                {appointment?.description}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>Cena wizyty:</p>
                            <p className={styles.description}>
                                {appointment?.price + "zł"}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                Data rozpoczęcia wizyty:
                            </p>
                            <p className={styles.description}>{startDate}</p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                Data zakończenia wizyty:
                            </p>
                            <p className={styles.description}>{endDate}</p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>Status wizyty:</p>
                            <p className={styles.description}>
                                {appointment?.status}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <ActionButton
                                title="Edytuj wizyte"
                                color="purple"
                                icon={faInfoCircle}
                                onClick={() => console.log('essa')}
                            ></ActionButton>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

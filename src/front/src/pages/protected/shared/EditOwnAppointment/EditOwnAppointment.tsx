import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import {
    ChronoUnit,
    Instant,
    LocalDateTime,
    ZoneId,
    ZoneOffset,
} from "@js-joda/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import ReactLoading from "react-loading";
import { useNavigate, useParams } from "react-router";
import {
    AppointmentListElementDto,
    editOwnAppointment,
    getAppointmentDetails,
} from "../../../../api/mop";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import { failureNotificationItems } from "../../../../utils/showNotificationsItems";
import styles from "./style.module.scss";

export const EditOwnAppointment = () => {
    const aLevel = useStoreSelector((state) => state.user.cur);
    const [appointment, setAppointment] = useState<AppointmentListElementDto>();
    const [etag, setEtag] = useState<string>("");
    const [inputStatus, setInputStatus] = useState<string>('')
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    const [inputDescription, setInputDescription] = useState<string>("");
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
        setEtag(data.etag);
        setInputStatus(data.data.status)
        setInputDescription(data.data.description);
        setAppointment(data.data);
        setLoading({ pageLoading: false, actionLoading: false });
    };
    useEffect(() => {
        handleGetAppointmentDetails();
    }, []);
    const handleEditOwnAppointment = async () => {
        if (!id || !etag || !appointment) return;
        let status;
        if (aLevel === "SPECIALIST") status = inputStatus;
        else status = appointment.status;
        const version = appointment.version;
        let description;
        if (aLevel === "SPECIALIST") description = inputDescription;
        else description = appointment.description;
        let editedDate = LocalDateTime.parse(startDate).toInstant(
            ZoneOffset.of(ZoneId.UTC.id())
        );
        editedDate = editedDate.minus(2, ChronoUnit.HOURS)
        const data = await editOwnAppointment({
            id,
            etag,
            status,
            version,
            description,
            startDate: editedDate,
        });
        if ("errorMessage" in data) {
            showNotification(failureNotificationItems(data.errorMessage));
            return;
        }
        navigate('/visits')
    };
    useEffect(() => {
        if (!appointment) return;
        const startDate = Instant.parse(appointment.startDate);
        const fixedStartDate = LocalDateTime.ofInstant(startDate);
        const endDate = Instant.parse(appointment.endDate);
        const fixedEndDate = LocalDateTime.ofInstant(endDate);
        setStartDate(fixedStartDate.truncatedTo(ChronoUnit.MINUTES).toString());
        setEndDate(fixedEndDate.truncatedTo(ChronoUnit.MINUTES).toString());
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
                        Edytuj wizytę
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
                            {aLevel === "SPECIALIST" && (
                                <input className={styles.description_input}
                                    type="text"
                                    value={inputDescription}
                                    onChange={(e) =>
                                        setInputDescription(e.target.value)
                                    }
                                />
                            )}
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
                            <input
                                type="datetime-local"
                                onChange={(e) => {
                                    setStartDate(e.target.value);
                                }}
                            />
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
                            {aLevel === "SPECIALIST" && (
                                <>
                                <input
                                    type="checkbox"
                                    value={inputDescription}
                                    onChange={(e) =>
                                        {
                                        if(e.target.value) setInputStatus('ACCEPTED')
                                        else {setInputStatus('PENDIG')}
                                    }
                                    }
                                />
                                <p className={styles.title}>Akceptuj wizytę</p>
                                </>
                            )}
                        </div>
                        <div className={styles.detail_wrapper}>
                            {
                                <ActionButton
                                    title="Prześlij zmianę"
                                    color="purple"
                                    icon={faInfoCircle}
                                    onClick={() => handleEditOwnAppointment()}
                                ></ActionButton>
                            }
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

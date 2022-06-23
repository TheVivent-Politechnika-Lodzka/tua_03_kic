import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { ChronoUnit, Instant, LocalDateTime } from "@js-joda/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import ReactLoading from "react-loading";
import { useNavigate, useParams } from "react-router";
import ActionButton from "../../components/shared/ActionButton/ActionButton";
import { useStoreSelector } from "../../redux/reduxHooks";
import { failureNotificationItems } from "../../utils/showNotificationsItems";
import { useTranslation } from "react-i18next";
import styles from "./style.module.scss";
import { editAppointmentByAdmin, getAppointmentDetails } from "../../api";

export const EditAppointment = () => {
    const { t } = useTranslation();
    const aLevel = useStoreSelector((state) => state.user.cur);
    const [appointment, setAppointment] = useState<AppointmentDto>();
    const [count, setCount] = useState<number>(0);
    const [etag, setEtag] = useState<string>("");
    const [inputStatus, setInputStatus] = useState<Status>("PENDING");
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
        setInputStatus(data.status);
        setInputDescription(data.description);
        setAppointment(data);
        setLoading({ pageLoading: false, actionLoading: false });
    };
    useEffect(() => {
        handleGetAppointmentDetails();
    }, []);
    const handleEditAppointment = async () => {
        if (!id || !etag || !appointment) return;
        let status = inputStatus;
        const version = appointment.version;
        let description = inputDescription;
        const data = await editAppointmentByAdmin({
            id,
            etag,
            status,
            version,
            description,
        });
        if ("errorMessage" in data) {
            showNotification(failureNotificationItems(data.errorMessage));
            return;
        }
        navigate("/visits");
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
                        {t("editOwnAppointment.title")}
                    </p>
                    <div className={styles.details_wrapper}>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {t("editOwnAppointment.clientName")}
                            </p>
                            <p className={styles.description}>
                                {appointment?.client.firstName +
                                    " " +
                                    appointment?.client.lastName}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {t("editOwnAppointment.specialistName")}
                            </p>
                            <p className={styles.description}>
                                {appointment?.specialist.firstName +
                                    " " +
                                    appointment?.specialist.lastName}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {t("editOwnAppointment.email")}
                            </p>
                            <p className={styles.description}>
                                {appointment?.specialist.email}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {t("editOwnAppointment.price")}
                            </p>
                            <p className={styles.description}>
                                {appointment?.price + "zł"}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {t("editOwnAppointment.dateStart")}
                            </p>
                            <p className={styles.description}>{startDate}</p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {t("editOwnAppointment.dateEnd")}
                            </p>
                            <p className={styles.description}>{endDate}</p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {t("editOwnAppointment.status")}
                            </p>
                            <p className={styles.description}>
                                {appointment?.status}
                            </p>
                            <input
                                className={styles.input_checkbox}
                                type="checkbox"
                                value={inputDescription}
                                onChange={(e) => {
                                    if (e.target.value)
                                        setInputStatus("PENDING");
                                }}
                            />
                            <p className={styles.title}>
                                {t("editOwnAppointment.cancel")}
                            </p>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <p className={styles.title}>
                                {t("editOwnAppointment.description")}
                            </p>
                            <textarea
                                        className={styles.description_input}
                                        value={inputDescription}
                                        maxLength={950}
                                        onChange={(e) => {
                                            setInputDescription(e.target.value);
                                            setCount(e.target.value.length);
                                        }}
                                    />
                                     <div className={styles.description_length}>
                                        {count}/950
                                    </div>
                        </div>
                        <div className={styles.detail_wrapper}>
                            <ActionButton
                                title={t("editOwnAppointment.button")}
                                color="purple"
                                icon={faInfoCircle}
                                onClick={() => handleEditAppointment()}
                            />
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
};

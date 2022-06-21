import { faClose, faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ChronoUnit, Instant, LocalDateTime } from "@js-joda/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import ReactLoading from "react-loading";
import {
    AppointmentListElementDto,
    getAppointmentDetails,
} from "../../../../api/mop";
import ImplantDetails from "../../../../components/ImplantDetails/ImplantDetails";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import Modal from "../../../../components/shared/Modal/Modal";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import { failureNotificationItems } from "../../../../utils/showNotificationsItems";
import styles from "./style.module.scss";
import { useNavigate } from "react-router-dom";
import { useTranslation } from "react-i18next";
interface AccountDetailsProps {
    isOpened: boolean;
    appointmentId: string;
    onClose: () => void;
}
export const AppointmentDetails = ({
    isOpened,
    appointmentId,
    onClose,
}: AccountDetailsProps) => {
    const aLevel = useStoreSelector((state) => state.user.cur);
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });
    let navigate = useNavigate();
    const routeChange = (id: string) => {
        let path = `/visit/edit/${id}`;
        navigate(path);
    };
    const [implantModal, setImplantModal] = useState<boolean>(false);
    const [appointment, setAppointment] = useState<AppointmentListElementDto>();
    const [startDate, setStartDate] = useState<string>("");
    const [endDate, setEndDate] = useState<string>("");
    const [implantId, setImplantId] = useState<string>("");
    const handleGetAppointmentDetails = async () => {
        const data = await getAppointmentDetails(appointmentId);
        if ("errorMessage" in data) {
            showNotification(failureNotificationItems(data.errorMessage));
            return;
        }
        setAppointment(data.data);
        setImplantId(data.data.implant.id);
        setLoading({ pageLoading: false, actionLoading: false });
    };

    const { t } = useTranslation();

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
    useEffect(() => {
        handleGetAppointmentDetails();
    }, []);

    return (
        <Modal isOpen={isOpened}>
            <section className={styles.account_details_page}>
                {loading.pageLoading ? (
                    <ReactLoading
                        type="cylon"
                        color="#fff"
                        width="10rem"
                        height="10rem"
                        className={styles.loading}
                    />
                ) : (
                    <div className={styles.content}>
                        <FontAwesomeIcon
                            className={styles.close_icon}
                            icon={faClose}
                            onClick={onClose}
                        />
                        <div className={styles.account_details}>
                            <p className={styles.account_details_title}>
                                {t("appointmentDetails.title")}
                            </p>
                            <div className={styles.details_wrapper}>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {aLevel === "SPECIALIST"
                                            ? t("appointmentDetails.clientName")
                                            : t(
                                                  "appointmentDetails.specialistName"
                                              )}
                                    </p>
                                    <p className={styles.description}>
                                        {aLevel === "SPECIALIST"
                                            ? appointment?.client.firstName +
                                              " " +
                                              appointment?.client.lastName
                                            : appointment?.specialist
                                                  .firstName +
                                              " " +
                                              appointment?.specialist.lastName}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("appointmentDetails.email")}
                                    </p>
                                    <p className={styles.description}>
                                        {appointment?.specialist.email}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("appointmentDetails.status")}{" "}
                                    </p>
                                    <p className={styles.description}>
                                        {appointment?.status}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {" "}
                                        {t("appointmentDetails.price")}
                                    </p>
                                    <p className={styles.description}>
                                        {appointment?.price + "zł"}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("appointmentDetails.dateStart")}{" "}
                                    </p>
                                    <p className={styles.description}>
                                        {startDate}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("appointmentDetails.dateEnd")}{" "}
                                    </p>
                                    <p className={styles.description}>
                                        {endDate}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("appointmentDetails.implant")}{" "}
                                    </p>
                                    <div className={styles.description}>
                                        <ActionButton
                                            title={t(
                                                "appointmentDetails.button"
                                            )}
                                            color="cyan"
                                            icon={faInfoCircle}
                                            onClick={() =>
                                                setImplantModal(true)
                                            }
                                        ></ActionButton>
                                    </div>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {" "}
                                        {t("appointmentDetails.description")}
                                    </p>
                                    <p className={styles.description}>
                                        {appointment?.description}
                                    </p>
                                </div>
                            </div>
                            <div className={styles.button_holder}>
                                <ActionButton
                                    title={t("appointmentDetails.buttonCancel")}
                                    color="cyan"
                                    icon={faInfoCircle}
                                    onClick={() => console.log("essa")}
                                ></ActionButton>
                                <ActionButton
                                    title={t("appointmentDetails.buttonEdit")}
                                    color="cyan"
                                    icon={faInfoCircle}
                                    onClick={() => routeChange(appointmentId)}
                                ></ActionButton>
                            </div>
                        </div>
                        <ImplantDetails
                            id={implantId}
                            isOpened={implantModal}
                            onClose={() => {
                                setImplantModal(false);
                            }}
                        />
                    </div>
                )}
            </section>
        </Modal>
    );
};

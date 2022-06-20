import { faClose } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ChronoUnit, Instant, LocalDateTime } from "@js-joda/core";
import { AppointmentListElementDto } from "../../../../api/mop";
import Modal from "../../../../components/shared/Modal/Modal";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import styles from "./style.module.scss";
interface AccountDetailsProps {
    isOpened: boolean;
    appointment: AppointmentListElementDto;
    onClose: () => void;
}
export const AppointmentDetails = ({
    isOpened,
    appointment,
    onClose,
}: AccountDetailsProps) => {
    const aLevel = useStoreSelector((state) => state.user.cur);
    const startDate = Instant.parse(appointment.startDate);
    const fixedStartDate = LocalDateTime.ofInstant(startDate);
    const endDate = Instant.parse(appointment.endDate);
    const fixedEndDate = LocalDateTime.ofInstant(endDate);
    return (
        <Modal isOpen={isOpened}>
            <section className={styles.account_details_page}>
                <div className={styles.content}>
                    <FontAwesomeIcon
                        className={styles.close_icon}
                        icon={faClose}
                        onClick={onClose}
                    />
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
                                <p className={styles.title}>
                                    Email kontaktowy:
                                </p>
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
                                    {appointment?.price}
                                </p>
                            </div>
                            <div className={styles.detail_wrapper}>
                                <p className={styles.title}>
                                    Data rozpoczęcia wizyty:
                                </p>
                                <p className={styles.description}>
                                    {fixedStartDate
                                        .truncatedTo(ChronoUnit.MINUTES)
                                        .toString()
                                        .replace("T", " ")}
                                </p>
                            </div>
                            <div className={styles.detail_wrapper}>
                                <p className={styles.title}>
                                    Data zakończenia wizyty:
                                </p>
                                <p className={styles.description}>
                                    {fixedEndDate
                                        .truncatedTo(ChronoUnit.MINUTES)
                                        .toString()
                                        .replace("T", " ")}
                                </p>
                            </div>
                            <div className={styles.detail_wrapper}>
                                <p className={styles.title}>
                                    Szczegóły implantu:
                                </p>
                                <p className={styles.description}>DODAĆ Szczegóły</p>
                            </div>
                            <div className={styles.detail_wrapper}>
                                <p className={styles.title}>Status wizyty:</p>
                                <p className={styles.description}>{appointment?.status}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </Modal>
    );
};

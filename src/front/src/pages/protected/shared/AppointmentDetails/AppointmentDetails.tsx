import { faClose, faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ChronoUnit, Instant, LocalDateTime } from "@js-joda/core";
import { useState } from "react";
import { AppointmentListElementDto } from "../../../../api/mop";
import ImplantDetails from "../../../../components/ImplantDetails/ImplantDetails";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
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
    const [implantModal, setImplantModal] = useState<boolean>(false);
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
                                    Obecnie wybrany implant:
                                </p>
                                <p className={styles.description}>
                                    <ActionButton
                                        title="Wyświetl"
                                        color="purple"
                                        icon={faInfoCircle}
                                        onClick={() => setImplantModal(true)}
                                    ></ActionButton>
                                </p>
                            </div>
                            <div className={styles.detail_wrapper}>
                                <p className={styles.title}>Status wizyty:</p>
                                <p className={styles.description}>
                                    {appointment?.status}
                                </p>
                            </div>
                        </div>
                        <div className={styles.button_holder}>
                            <ActionButton //TODO dodać implementacje takowych przycisków, przyciski są w celu ustalenia stylowania
                                title="Odwołaj wizyte"
                                color="purple"
                                icon={faInfoCircle}
                                onClick={() => console.log("essa")}
                            ></ActionButton>
                            <ActionButton
                                title="Edytuj wizyte"
                                color="purple"
                                icon={faInfoCircle}
                                onClick={() => console.log("essa")}
                            ></ActionButton>
                        </div>
                    </div>
                    <ImplantDetails
                        id={appointment.implant.id}
                        isOpened={implantModal}
                        onClose={() => {
                            setImplantModal(false);
                        }}
                    />
                </div>
            </section>
        </Modal>
    );
};

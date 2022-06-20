import { faInfoCircle } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { AppointmentListElementDto } from "../../api/mop";
import { useStoreSelector } from "../../redux/reduxHooks";
import styles from "./style.module.scss";
import { LocalDateTime , Instant, ChronoUnit} from '@js-joda/core'
interface AppointmentRecordProps {
    appointment: AppointmentListElementDto;
}

export const AppointmentRecord = ({ appointment}: AppointmentRecordProps) => {
    
    const aLevel = useStoreSelector((state) => state.user.cur);
    const date = Instant.parse(appointment.startDate)
    const fixedDate = LocalDateTime.ofInstant(date);

    return (
        <div className={styles.appointment_record_wrapper}>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>{(aLevel === "SPECIALIST") ? appointment?.client.firstName + ' ' + appointment?.client.lastName : appointment?.specialist.firstName + ' ' + appointment?.specialist.lastName}</p>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>{appointment?.implant.name}</p>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>{appointment?.status}</p>
            </div>
            <div className={styles.detail_wrapper}>
                <p className={styles.detail}>{fixedDate.truncatedTo(ChronoUnit.MINUTES).toString().replace("T", ' ')}</p>
            </div>

            <div className={styles.detail_wrapper}>
                <div className={styles.info_button}>
                    <FontAwesomeIcon icon={faInfoCircle} />
                </div>
            </div>
        </div>
    );
};

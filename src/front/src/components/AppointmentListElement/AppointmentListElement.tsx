import {SimpleGrid, Grid} from "@mantine/core";
import {useTranslation} from "react-i18next";
import {AppointmentListElementDto} from "../../api/mop"
import {BlueGradientButton} from "../Button/BlueGradientButton copy";
import styles from "./listElement.module.scss";

export const AppointmentListElement = (props: { element: AppointmentListElementDto }) => {
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <SimpleGrid cols={3} >
                <SimpleGrid cols={3}>
                    <div className={styles.image_top}>
                        <img src={props.element.client_url} height="40px" alt="img"/>
                    </div>
                    <div className={styles.image_bottom} >
                        <img src={props.element.specialist_url} height="40px" alt="img"/>
                    </div>
                    <div />
                </SimpleGrid>

                <div className={styles.text} >
                    <p
                        className={styles.description}
                    >{`${props.element.description.substring(0, 200)}${
                        props.element.description.length < 200 ? "" : "..."
                    }`}</p>
                </div>
                <div className={styles.buttons}>
                    <div className={styles.button}>
                        <BlueGradientButton
                            label={t("appointmentListPage.listElement.details")}
                            onClick={() => console.log(props.element.id)}
                        />
                    </div>
                </div>
            </SimpleGrid>
        </div>
    );
};

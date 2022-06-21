import {SimpleGrid, Grid} from "@mantine/core";
import {useTranslation} from "react-i18next";
import {AppointmentListElementDto} from "../../api/mop"
import {BlueGradientButton} from "../Button/BlueGradientButton";

import styles from "./listElement.module.scss";
import {AppointmentDetails} from "../../pages/protected/shared/AppointmentDetails";
import {useState} from "react";

export const AppointmentListElement = (props: { element: AppointmentListElementDto }) => {

    const [modal, setModal] = useState<boolean>(false);
    const {t} = useTranslation();

    return (
        <div className={styles.container}>
            <SimpleGrid cols={3}>
                <SimpleGrid cols={3}>
                    <div className={styles.image_top}>
                        <img src={props.element.client.url} height="170px" alt="img"/>
                    </div>
                    <div className={styles.image_bottom}>
                        <img src={props.element.specialist.url} height="130px" alt="img"/>
                    </div>
                    <div/>
                </SimpleGrid>

                <div className={styles.text}>
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
                            onClick={
                                () => {
                                    console.log(props.element.id)
                                    setModal(true);
                                }
                            }
                        />
                    </div>
                </div>
            </SimpleGrid>
            <AppointmentDetails
                isOpened={modal}
                appointmentId={props.element.id}
                onClose={ () => {
                    setModal(false);
                }}
            />
        </div>
    );
};

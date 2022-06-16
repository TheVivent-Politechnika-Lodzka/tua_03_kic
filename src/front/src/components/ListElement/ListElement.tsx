import { SimpleGrid, Container } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { ImplantListElementDto } from "../../api/mop";
import { BlueGradientButton } from "../Button/BlueGradientButton copy";
import styles from "./listElement.module.scss";

export const ListElement = (props: { element: ImplantListElementDto }) => {
    const {t} =useTranslation();

    return (
        <div className={styles.container}>
            <SimpleGrid cols={3}>
                <div className={styles.image}>
                    <img src={props.element.url} height="50px" alt="img" />
                </div>
                <div className={styles.text}>
                    <p
                        className={styles.title}
                    >{`${props.element.name.substring(0, 20)}${
                        props.element.name.length < 50 ? "" : "..."
                    }`}</p>
                    <p
                        className={styles.description}
                    >{`${props.element.description.substring(0, 200)}${
                        props.element.description.length < 200 ? "" : "..."
                    }`}</p>
                </div>
                <div className={styles.buttons}>
                    <div className={styles.price}>{`${
                        props.element.price
                    }${" ZL"}`}</div>
                    <div className={styles.button}>
                        <BlueGradientButton
                            label={t("implantListPage.listElement.details")}
                            onClick={() => console.log(props.element.id)}
                        />
                    </div>
                </div>
            </SimpleGrid>
        </div>
    );
};

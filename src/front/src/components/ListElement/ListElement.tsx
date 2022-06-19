import { SimpleGrid, Container, Center, Image } from "@mantine/core";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ImplantListElementDto } from "../../api/mop";
import { BlueGradientButton } from "../Button/BlueGradientButton";
import ImplantDetails from "../ImplantDetails/ImplantDetails";

import styles from "./listElement.module.scss";

export const ListElement = (props: { element: ImplantListElementDto }) => {
    const { t } = useTranslation();

    const [modal, setModal] = useState<boolean>(false);

    useEffect(() => {}, [modal]);
    return (
        <div className={styles.container}>
            <SimpleGrid cols={3}>
                <div>
                    {props.element.url ? (
                        <Image
                            radius="md"
                            src={props.element.url}
                            alt="img"
                            height="18vh"
                            styles={{
                                root: { paddingTop: "2vh", paddingLeft: "2vh" },
                            }}
                        />
                    ) : (
                        <Image
                            radius="md"
                            src="brak.jpg"
                            alt="img"
                            height="18vh"
                            styles={{
                                root: { paddingTop: "2vh", paddingLeft: "2vh" },
                            }}
                        />
                    )}
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
                    <div className={styles.price}>
                        {`${props.element.price}${" ZL"}`}
                    </div>

                    <div className={styles.button}>
                        <BlueGradientButton
                            label={t("implantListPage.listElement.details")}
                            onClick={() => {
                                setModal(true);
                            }}
                        />
                    </div>
                    <ImplantDetails
                        id={props.element.id}
                        isOpened={modal}
                        onClose={() => {
                            setModal(false);
                        }}
                    />
                </div>
            </SimpleGrid>
        </div>
    );
};

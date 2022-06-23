import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { SimpleGrid, Container, Center, Image } from "@mantine/core";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import { ImplantListElementDto } from "../../api/mop";
import ImplantDetails from "../ImplantDetails/ImplantDetails";
import ActionButton from "../shared/ActionButton/ActionButton";

import styles from "./listElement.module.scss";

export const ListElement = (props: { element: ImplantListElementDto }) => {
    const { t } = useTranslation();

    const [modal, setModal] = useState<boolean>(false);

    useEffect(() => {}, [modal]);
    return (
        <div className={styles.container}>
            <SimpleGrid cols={3}>
                <div>
                    <Center>
                        {props.element.url ? (
                            <Image
                                radius="md"
                                src={props.element.url}
                                alt="img"
                                height="18vh"
                                width="35vh"
                                styles={{
                                    root: {
                                        paddingTop: "2vh",
                                        paddingLeft: "2vh",
                                    },
                                }}
                            />
                        ) : (
                            <Image
                                radius="md"
                                src="brak.jpg"
                                alt="img"
                                height="18vh"
                                width="35vh"
                                styles={{
                                    root: {
                                        paddingTop: "2vh",
                                        paddingLeft: "2vh",
                                    },
                                }}
                            />
                        )}{" "}
                    </Center>
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
                        <ActionButton
                            onClick={() => setModal(true)}
                            title={t("implantListPage.listElement.details")}
                            color="blue"
                            icon={faCheck}
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

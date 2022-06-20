import { faClose, faEdit, faFolder } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { useEffect, useState } from "react";
import ReactModal from "react-modal";
import ReactLoading from "react-loading";
import ActionButton from "../shared/ActionButton/ActionButton";
import styles from "./style.module.scss";
import { showNotification } from "@mantine/notifications";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../utils/showNotificationsItems";
import { archiveImplant, getImplant, GetImplantResponse } from "../../api/mop";
import { useStoreSelector } from "../../redux/reduxHooks";
import ConfirmActionModal from "../shared/ConfirmActionModal/ConfirmActionModal";

interface ImplantDetailsProps {
    id: string;
    isOpened: boolean;
    onClose: () => void;
}

const ImplantDetails = ({ id, isOpened, onClose }: ImplantDetailsProps) => {
    const [implant, setImplant] = useState<GetImplantResponse>();
    const accessLevel = useStoreSelector((state) => state.user.cur);
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });

    const handleGetImplant = async () => {
        const response = await getImplant(id);
        if ("errorMessage" in response) {
            setLoading({ pageLoading: false });
            showNotification(failureNotificationItems(response?.errorMessage));
            return;
        }
        setImplant(response);
        setLoading({ pageLoading: false });
    };

    const [opened, setOpened] = useState<boolean>(false);

    const customStyles = {
        overlay: {
            backgroundColor: "rgba(0, 0, 0, 0.85)",
        },
        content: {
            width: "100vw",
            height: "100vh",
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            backgroundColor: "transparent",
            border: "none",
        },
    };

    const handleArchiveImpland = async () => {
        if (!implant) return;
        setLoading({ ...loading, actionLoading: true });
        console.log(implant.etag);
        console.log(implant.id);
        const response = await archiveImplant(implant.id, implant.etag);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            setLoading({ ...loading, actionLoading: false });
            return;
        }
        showNotification(successNotficiationItems("account.edit.success"));
        setImplant(response);

        setLoading({ ...loading, actionLoading: false });
    };

    useEffect(() => {
        handleGetImplant();
    }, [isOpened]);

    return (
        <ReactModal isOpen={isOpened} style={customStyles} ariaHideApp={false}>
            <section className={styles.implant_details_page}>
                {loading.pageLoading ? (
                    <ReactLoading
                        type="cylon"
                        color="#fff"
                        width="10rem"
                        height="10rem"
                    />
                ) : (
                    <div className={styles.content}>
                        <FontAwesomeIcon
                            className={styles.close_icon}
                            icon={faClose}
                            onClick={onClose}
                        />

                        <div className={styles.implant_details}>
                            <div className={styles.top_wrapper}>
                                <div className={styles.detail_top_wrapper}>
                                    <p className={styles.implant_details_title}>
                                        Dane szczegółowe
                                    </p>
                                    <p className={styles.description}>
                                        {implant?.description}
                                    </p>
                                </div>
                                <div className={styles.detail_top_wrapper}>
                                    <img
                                        className={styles.image}
                                        src={implant?.image}
                                        alt="Implant image"
                                    />
                                </div>
                            </div>
                            <div className={styles.details_wrapper}>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Nazwa:</p>
                                    <p className={styles.description}>
                                        {implant?.name}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Cena:</p>
                                    <p className={styles.description}>
                                        {implant?.price} zl
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Producent:</p>
                                    <p className={styles.description}>
                                        {implant?.manufacturer}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        Czas instalacji:
                                    </p>
                                    <p className={styles.description}>
                                        {implant?.duration &&
                                            Math.round(implant?.duration / 60) +
                                                " min"}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Zastosowano:</p>
                                    <p className={styles.description}>
                                        {implant?.popularity} razy
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>Stan: </p>
                                    <p
                                        className={
                                            implant?.archived
                                                ? styles.inactive
                                                : styles.active
                                        }
                                    >
                                        {implant?.archived
                                            ? "Niedostępny"
                                            : "Dostępny"}
                                    </p>
                                </div>
                            </div>
                            {accessLevel === "ADMINISTRATOR" && (
                                <div className={styles.action_wrapper}>
                                    <div className={styles.button}>
                                        <ActionButton
                                            title="Edytuj"
                                            icon={faEdit}
                                            color="green"
                                            onClick={() => {}}
                                        />
                                    </div>
                                    <div className={styles.button}>
                                        <ActionButton
                                            title="Archiwizuj"
                                            icon={faFolder}
                                            color="purple"
                                            onClick={() => {
                                                setOpened(true);
                                            }}
                                        />
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                )}
                <ConfirmActionModal
                    isOpened={opened}
                    onClose={() => {
                        setOpened(false);
                    }}
                    handleFunction={async () => {
                        await handleArchiveImpland();
                        setOpened(false);
                    }}
                    isLoading={loading.actionLoading as boolean}
                    title="Archiwizacja implantu"
                >
                    Czy na pewno chcesz zarchiwizować implant? Operacja jest
                    nieodwracalna!
                </ConfirmActionModal>
            </section>
        </ReactModal>
    );
};

export default ImplantDetails;

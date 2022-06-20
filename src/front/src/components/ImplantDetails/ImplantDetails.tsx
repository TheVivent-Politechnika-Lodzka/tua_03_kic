import {
    faClose,
    faEdit,
    faShoppingCart,
} from "@fortawesome/free-solid-svg-icons";
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
import { getImplant, GetImplantResponse } from "../../api/mop";
import { useStoreSelector } from "../../redux/reduxHooks";
import { useNavigate } from "react-router";

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
    const navigate = useNavigate();

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
                                <ActionButton
                                    title="Rezerwuj"
                                    icon={faShoppingCart}
                                    onClick={() => {
                                        navigate(
                                            `/implants/${implant?.id}/create-appointment`
                                        );
                                    }}
                                    color="green"
                                />
                            </div>
                            {accessLevel === "ADMINISTRATOR" && (
                                <div className={styles.action_wrapper}>
                                    <ActionButton
                                        title="Edytuj"
                                        icon={faEdit}
                                        color="green"
                                        onClick={() => {}}
                                    />
                                </div>
                            )}
                        </div>
                    </div>
                )}
            </section>
        </ReactModal>
    );
};

export default ImplantDetails;

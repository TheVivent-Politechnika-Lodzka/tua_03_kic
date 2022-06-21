import {
    faClose,
    faEdit,
    faShoppingCart,
    faFolder,
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
import { archiveImplant, getImplant, GetImplantResponse } from "../../api/mop";
import { useStoreSelector } from "../../redux/reduxHooks";
import ConfirmActionModal from "../shared/ConfirmActionModal/ConfirmActionModal";
import { useNavigate } from "react-router";
import { useTranslation } from "react-i18next";

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

    const { t } = useTranslation();

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
        showNotification(
            successNotficiationItems(
                t("implantDetails.successNotficiationItems")
            )
        );
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
                                        {t("implantDetails.title")}
                                    </p>
                                    <p className={styles.description}>
                                        {implant?.description}
                                    </p>
                                </div>
                                <div className={styles.detail_top_wrapper}>
                                    <img
                                        className={styles.image}
                                        src={implant?.image}
                                        alt={t("implantDetails.alt")}
                                    />
                                </div>
                            </div>
                            <div className={styles.details_wrapper}>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("implantDetails.name")}
                                    </p>
                                    <p className={styles.description}>
                                        {implant?.name}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("implantDetails.price")}
                                    </p>
                                    <p className={styles.description}>
                                        {implant?.price} zl
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("implantDetails.producer")}
                                    </p>
                                    <p className={styles.description}>
                                        {implant?.manufacturer}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("implantDetails.time")}
                                    </p>
                                    <p className={styles.description}>
                                        {implant?.duration &&
                                            Math.round(implant?.duration / 60) +
                                                " min"}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("implantDetails.applied")}
                                    </p>
                                    <p className={styles.description}>
                                        {implant?.popularity}{" "}
                                        {t("implantDetails.times")}
                                    </p>
                                </div>
                                <div className={styles.detail_wrapper}>
                                    <p className={styles.title}>
                                        {t("implantDetails.state")}{" "}
                                    </p>
                                    <p
                                        className={
                                            implant?.archived
                                                ? styles.inactive
                                                : styles.active
                                        }
                                    >
                                        {implant?.archived
                                            ? t("implantDetails.available")
                                            : t("implantDetails.notAvailable")}
                                    </p>
                                </div>
                            </div>
                            {accessLevel === "CLIENT" && (
                                <div className={styles.action_wrapper}>
                                    <div className={styles.button}>
                                        <ActionButton
                                            title={t(
                                                "implantDetails.reserveButton"
                                            )}
                                            icon={faShoppingCart}
                                            onClick={() => {
                                                navigate(
                                                    `/implants/${implant?.id}/create-appointment`
                                                );
                                            }}
                                            color="green"
                                        />
                                    </div>
                                </div>
                            )}
                            {accessLevel === "ADMINISTRATOR" && (
                                <div className={styles.action_wrapper}>
                                    <div className={styles.button}>
                                        <ActionButton
                                            title={t("implantDetails.edit")}
                                            icon={faEdit}
                                            color="green"
                                            onClick={() =>
                                                navigate(
                                                    `/implant/${implant?.id}`
                                                )
                                            }
                                        />
                                    </div>
                                    <div className={styles.button}>
                                        <ActionButton
                                            title={t("implantDetails.archive")}
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
                    title={t("implantDetails.confirmActionModal.title")}
                >
                    {t("implantDetails.confirmActionModal.message")}
                </ConfirmActionModal>
            </section>
        </ReactModal>
    );
};

export default ImplantDetails;

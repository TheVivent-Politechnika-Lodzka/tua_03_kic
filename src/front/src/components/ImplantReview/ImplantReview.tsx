import { faTrashAlt } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { showNotification } from "@mantine/notifications";
import { Rating } from "@mui/material";
import { useState } from "react";
import { deleteImplantsReview } from "../../api";
import { useStoreSelector } from "../../redux/reduxHooks";
import { successNotficiationItems } from "../../utils/showNotificationsItems";
import ConfirmActionModal from "../shared/ConfirmActionModal/ConfirmActionModal";
import styles from "./style.module.scss";
import { useTranslation } from "react-i18next";

interface ImplantReviewProps {
    review: ImplantReview;
    onClose: () => void;
}

const ImplantReview = ({ review, onClose }: ImplantReviewProps) => {
    const { t } = useTranslation();
    const [modal, setModal] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(false);

    const accessLevel = useStoreSelector((state) => state.user.cur);
    const login = useStoreSelector((state) => state.user.sub);

    const handleDeleteImplantReview = async () => {
        if (!review.id) return;
        setLoading(true);
        await deleteImplantsReview(review.id);
        setLoading(false);
        setModal(false);
        onClose();
        showNotification(
            successNotficiationItems(t("addImplantReviewPage.reviewDeleted"))
        );
    };

    return (
        <div className={styles.implant_review}>
            <div className={styles.general_info_wrapper}>
                <img
                    className={styles.avatar}
                    src={review?.clientImage}
                    alt="user_avatar"
                />
                <div className={styles.stars}>
                    <Rating
                        value={review?.rating}
                        readOnly={true}
                        size="large"
                    />
                </div>
            </div>
            <div className={styles.review_description_wrapper}>
                <p className={styles.user_login}>{review?.clientLogin}</p>
                <div className={styles.description_wrapper}>
                    <p className={styles.description}>{review?.review}</p>
                </div>
            </div>
            {(accessLevel === "ADMINISTRATOR" ||
                review.clientLogin === login) && (
                <div className={styles.delete_button}>
                    <FontAwesomeIcon
                        icon={faTrashAlt}
                        className={styles.icon}
                        onClick={() => {
                            setModal(true);
                        }}
                    />
                </div>
            )}
            <ConfirmActionModal
                isOpened={modal}
                onClose={() => {
                    setModal(false);
                }}
                handleFunction={handleDeleteImplantReview}
                title={t("addImplantReviewPage.removeReviewTitle")}
                isLoading={loading}
            >
                {t("addImplantReviewPage.confirmMsg2")}
            </ConfirmActionModal>
        </div>
    );
};

export default ImplantReview;

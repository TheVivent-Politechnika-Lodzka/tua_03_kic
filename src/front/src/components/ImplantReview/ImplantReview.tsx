import {
    faDeleteLeft,
    faRemove,
    faTrashAlt,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { showNotification } from "@mantine/notifications";
import { Rating } from "@mui/material";
import { Api } from "@reduxjs/toolkit/dist/query";
import { useState } from "react";
import { deleteImplantReview } from "../../api";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../utils/showNotificationsItems";
import ConfirmActionModal from "../shared/ConfirmActionModal/ConfirmActionModal";
import styles from "./style.module.scss";

interface ImplantReviewProps {
    review: ImplantReview;
}

const ImplantReview = ({ review }: ImplantReviewProps) => {
    const [modal, setModal] = useState<boolean>(false);
    const [loading, setLoading] = useState<boolean>(false);

    const handleDeleteImplantReview = async () => {
        if (!review.id) return;
        setLoading(true);
        const data = await deleteImplantReview(review.id);
        if ("errorMessage" in data) {
            setLoading(false);
            showNotification(failureNotificationItems(data.errorMessage));
        }
        setLoading(false);
        setModal(false);
        showNotification(
            successNotficiationItems("Implant review deleted successfully")
        );
    };

    return (
        <div className={styles.implant_review}>
            <div className={styles.general_info_wrapper}>
                <img
                    className={styles.avatar}
                    src="https://media.discordapp.net/attachments/948268830222848183/988127000336138280/dgTUsgBf_400x400.jpg"
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
            <div className={styles.delete_button}>
                <FontAwesomeIcon
                    icon={faTrashAlt}
                    className={styles.icon}
                    onClick={() => {
                        setModal(true);
                    }}
                />
            </div>
            <ConfirmActionModal
                isOpened={modal}
                onClose={() => {
                    setModal(false);
                }}
                handleFunction={async () => {
                    await deleteImplantReview(review?.id);
                }}
                title="Usuń recenzję"
                isLoading={loading}
                children="Czy na pewno chcesz usunąć tą recenzję?"
            />
        </div>
    );
};

export default ImplantReview;

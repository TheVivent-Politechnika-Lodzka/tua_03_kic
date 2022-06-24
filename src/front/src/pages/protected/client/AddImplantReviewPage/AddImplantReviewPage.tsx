import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { Rating } from "@mui/material";
import { t } from "i18next";
import { useState } from "react";
import { useTranslation } from "react-i18next";
import { addImplantsReview } from "../../../../api";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import Modal from "../../../../components/shared/Modal/Modal";
import { useStoreSelector } from "../../../../redux/reduxHooks";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import styles from "./style.module.scss";

interface AddImplantReviewPageProps {
    onClose: () => void;
    isOpen: boolean;
    implantId: string;
}

const AddImplantReviewPage = ({
    isOpen,
    onClose,
    implantId,
}: AddImplantReviewPageProps) => {
    const [rate, setRate] = useState<number>(0);
    const [review, setReview] = useState<string>("");
    const [loading, setLoading] = useState<boolean>(false);

    const login = useStoreSelector((state) => state.user.sub);

    const { t } = useTranslation();

    const handleAddReview = async () => {
        if (!implantId) return;
        setLoading(true);
        const data = await addImplantsReview({
            implantId: implantId,
            rating: rate,
            review: review,
            clientLogin: login,
        });
        if ("errorMessage" in data) {
            setLoading(false);
            showNotification(failureNotificationItems(data.errorMessage));
            return;
        }
        setLoading(false);
        showNotification(
            successNotficiationItems(t("addImplantReview.reviewAdded"))
        );
        setRate(0);
        onClose();
    };

    return (
        <Modal isOpen={isOpen}>
            <div className={styles.add_implant_review_page}>
                <h1>{t("addImplantReviewPage.addImplantReview")}</h1>
                <Rating
                    value={rate}
                    precision={0.5}
                    onChange={(_, value) => {
                        setRate(value as number);
                    }}
                    size="large"
                />
                <textarea
                    className={styles.text_area}
                    value={review}
                    onChange={(e) => {
                        setReview(e.target.value);
                    }}
                />
                <div className={styles.action_wrapper}>
                    <ActionButton
                        title={t("addImplantReviewPage.confirm")}
                        color="green"
                        icon={faCheck}
                        isLoading={loading}
                        onClick={handleAddReview}
                    />
                    <ActionButton
                        onClick={onClose}
                        title={t("addImplantReviewPage.cancel")}
                        color="red"
                        icon={faCancel}
                    />
                </div>
            </div>
        </Modal>
    );
};
export default AddImplantReviewPage;

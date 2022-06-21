import { useEffect, useState } from "react";
import ImplantReview from "../../../../components/ImplantReview/ImplantReview";
import Modal from "../../../../components/shared/Modal/Modal";
import ReactLoading from "react-loading";
import styles from "./style.module.scss";
import {
    getImplant,
    GetImplantResponse,
    getImplantsReviews,
    ListImplantResponse,
    ListImplantReviewsResponse,
} from "../../../../api";
import { showNotification } from "@mantine/notifications";
import { failureNotificationItems } from "../../../../utils/showNotificationsItems";

interface ImplantReviewsListPageProps {
    isOpened: boolean;
    onClose: () => void;
    implantId: string;
}

const ImplantReviewsListPage = ({
    isOpened,
    onClose,
    implantId,
}: ImplantReviewsListPageProps) => {
    const [reviews, setReviews] = useState<ListImplantReviewsResponse>();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });

    const handleGetImplantReviews = async () => {
        if (!implantId) return;
        setLoading({ ...loading, pageLoading: true });
        const data = await getImplantsReviews(implantId, 1, 5);
        if ("errorMessage" in data) {
            setLoading({ ...loading, pageLoading: false });
            showNotification(failureNotificationItems(data.errorMessage));
            return;
        }
        setReviews(data);
        setLoading({ ...loading, pageLoading: false });
    };

    useEffect(() => {
        handleGetImplantReviews();
    }, [isOpened]);

    return (
        <Modal isOpen={isOpened}>
            {loading.pageLoading ? (
                <ReactLoading
                    type="bars"
                    color="#fff"
                    width="10rem"
                    height="10rem"
                />
            ) : (
                <div className={styles.implants_reviews_list_page}>
                    <div className={styles.reviews_wrapper}>
                        {reviews?.data.map((review) => (
                            <ImplantReview key={review?.id} review={review} />
                        ))}
                    </div>
                </div>
            )}
        </Modal>
    );
};

export default ImplantReviewsListPage;

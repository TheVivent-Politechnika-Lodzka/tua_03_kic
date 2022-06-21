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
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faClose } from "@fortawesome/free-solid-svg-icons";
import Pagination from "../../../../components/Pagination/Pagination";

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
    const [pagination, setPagination] = useState<Pagination>({
        currentPage: 1,
        pageSize: 1,
        totalPages: 1,
    });

    const handleGetImplantReviews = async () => {
        if (!implantId) return;
        setLoading({ ...loading, pageLoading: true });
        const data = await getImplantsReviews(
            implantId,
            pagination.currentPage as number,
            pagination.pageSize as number
        );
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
    }, [isOpened, pagination]);

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
                    <FontAwesomeIcon
                        className={styles.close_icon}
                        icon={faClose}
                        onClick={onClose}
                    />
                    <div className={styles.reviews_wrapper}>
                        {reviews?.data.map((review) => (
                            <ImplantReview key={review?.id} review={review} />
                        ))}
                        {reviews?.data.length !== 0 && (
                            <Pagination
                                pagination={pagination}
                                handleFunction={setPagination}
                            />
                        )}
                        {reviews?.data.length === 0 && (
                            <p className={styles.no_reviews}>
                                Implant nie posiada żadnych recenzji
                            </p>
                        )}
                    </div>
                </div>
            )}
        </Modal>
    );
};

export default ImplantReviewsListPage;

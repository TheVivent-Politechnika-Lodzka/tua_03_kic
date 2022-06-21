import ImplantReview from "../../../../components/ImplantReview/ImplantReview";
import Modal from "../../../../components/shared/Modal/Modal";
import styles from "./style.module.scss";

interface ImplantReviewsListPageProps {
    isOpened: boolean;
    onClose: () => void;
    implantId: string;
}

const ImplantReviewsListPage = ({
    isOpened,
    onClose,
    implantId
}: ImplantReviewsListPageProps) => {
    return (
        <Modal isOpen={isOpened}>
            <div className={styles.implants_reviews_list_page}>
                <div className={styles.reviews_wrapper}>
                    <ImplantReview />
                    <ImplantReview />
                    <ImplantReview />
                    <ImplantReview />
                    <ImplantReview />
                </div>
            </div>
        </Modal>
    );
};

export default ImplantReviewsListPage;

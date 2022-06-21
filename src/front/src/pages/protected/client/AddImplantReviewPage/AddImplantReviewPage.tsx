import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { Rating } from "@mui/material";
import { useState } from "react";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import Modal from "../../../../components/shared/Modal/Modal";
import styles from "./style.module.scss";

interface AddImplantReviewPageProps {
    onClose: () => void;
    isOpen: boolean;
}

const AddImplantReviewPage = ({
    isOpen,
    onClose,
}: AddImplantReviewPageProps) => {
    const [rate, setRate] = useState<number>(0);

    return (
        <Modal isOpen={isOpen}>
            <div className={styles.add_implant_review_page}>
                <h1>Add Implant Review</h1>
                <Rating
                    value={rate}
                    precision={0.5}
                    onChange={(_, value) => {
                        setRate(value as number);
                    }}
                />
                <textarea className={styles.text_area} />
                <div className={styles.action_wrapper}>
                    <ActionButton
                        onClick={onClose}
                        title="Confirm"
                        color="green"
                        icon={faCheck}
                    />
                    <ActionButton
                        onClick={onClose}
                        title="Cancel"
                        color="red"
                        icon={faCancel}
                    />
                </div>
            </div>
        </Modal>
    );
};
export default AddImplantReviewPage;

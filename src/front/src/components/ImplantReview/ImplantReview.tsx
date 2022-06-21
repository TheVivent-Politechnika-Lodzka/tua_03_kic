import { Rating } from "@mui/material";
import styles from "./style.module.scss";

interface ImplantReviewProps {
    review: ImplantReview;
}

const ImplantReview = ({review}: ImplantReviewProps) => {

    return (
        <div className={styles.implant_review}>
            <div className={styles.general_info_wrapper}>
                <img
                    className={styles.avatar}
                    src="https://media.discordapp.net/attachments/948268830222848183/988127000336138280/dgTUsgBf_400x400.jpg"
                    alt="user_avatar"
                />
                <div className={styles.stars}>
                    <Rating value={review?.rating} readOnly={true} size="large" />
                </div>
            </div>
            <div className={styles.review_description_wrapper}>
                <p className={styles.user_login}>{review?.clientLogin}</p>
                <div className={styles.description_wrapper}>
                    <p className={styles.description}>
                        {review?.review}
                    </p>
                </div>
            </div>
        </div>
    );
};

export default ImplantReview;

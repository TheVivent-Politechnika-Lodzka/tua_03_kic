import { Rating } from "@mui/material";
import Rater from "react-rater";
import styles from "./style.module.scss";

interface ImplantReviewProps {
    user: AccountDetails;
}

const ImplantReview = () => {
    return (
        <div className={styles.implant_review}>
            <div className={styles.general_info_wrapper}>
                <img
                    className={styles.avatar}
                    src="https://media.discordapp.net/attachments/948268830222848183/988127000336138280/dgTUsgBf_400x400.jpg"
                    alt="user_avatar"
                />
                <div className={styles.stars}>
                    <Rating value={5} readOnly={true} size="large" />
                </div>
            </div>
            <div className={styles.review_description_wrapper}>
                <p className={styles.user_login}>DrJohnKcoc2723</p>
                <div className={styles.description_wrapper}>
                    <p className={styles.description}>
                        Lorem ipsum dolor sit amet consectetur adipisicing elit.
                        Repellendus fuga repellat quisquam soluta! Velit porro
                        sapiente et beatae repudiandae voluptas optio molestiae
                        ducimus, facilis cum iure quidem iusto quo fugiat dicta
                        illum magnam id numquam temporibus eius modi ut eaque!
                        Ducimus, harum unde. Animi blanditiis numquam laborum
                        quos, exercitationem quasi saepe accusamus enim quam
                        illo impedit voluptatibus ducimus amet at fugiat,
                        similique voluptate cumque laboriosam doloribus
                        repudiandae officiis ea iure? Et asperiores magnam
                        dolore illo enim omnis optio dolorum, facilis
                        repellendus voluptatibus. Nihil explicabo culpa nisi
                        quam! Eius praesentium saepe doloremque facere animi sit
                        quisquam quidem corrupti officiis eaque cupiditate optio
                        deserunt accusantium magni reiciendis pariatur a ex
                        earum, ipsam, adipisci cumque, voluptate necessitatibus
                        aliquam. Voluptatibus assumenda adipisci at voluptas
                        nihil maiores, quam aliquid iste eius! Consectetur quod
                        maiores doloremque, repellat voluptatem dolore accusamus
                        possimus quidem? Eos laboriosam necessitatibus eligendi
                        ipsam unde commodi itaque similique modi qui quis
                        dolorem corrupti nesciunt atque aliquam magnam
                        voluptatibus laborum iure ratione, autem dolorum
                        nostrum, exercitationem sit cumque. Natus dolore nobis
                        porro culpa earum laborum ipsum quibusdam debitis,
                        deserunt animi quis amet veniam facilis quasi obcaecati
                        dolorem? Doloremque beatae omnis possimus repudiandae
                        temporibus praesentium, est, quod vel vitae accusantium
                        odit aliquam sequi. Non, quam.
                    </p>
                </div>
            </div>
        </div>
    );
};

export default ImplantReview;

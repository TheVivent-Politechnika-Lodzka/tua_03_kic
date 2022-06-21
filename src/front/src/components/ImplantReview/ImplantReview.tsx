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
                    <Rating value={5} readOnly={true} />
                </div>
            </div>
            <div className={styles.review_description_wrapper}>
                <p className={styles.user_login}>DrJohnKcoc2723</p>
                <p className={styles.description}>
                    Lorem ipsum dolor, sit amet consectetur adipisicing elit.
                    Maiores in impedit voluptatibus doloremque officiis. Magnam,
                    enim veritatis ducimus ipsa illum animi aut vero reiciendis
                    mollitia! Fugiat, commodi dolor. Nisi magnam, esse fugit at
                    facere ipsam soluta! Aperiam ipsa aspernatur libero magni
                    aliquam voluptatum debitis minima ducimus nihil quo fuga
                    saepe, suscipit iure, labore earum. Perspiciatis sed error
                    tenetur nulla doloribus excepturi sint autem expedita
                    ducimus iusto velit at atque, possimus voluptate natus
                    maiores nesciunt. Esse facere debitis dolore tenetur? Nobis
                    aut ducimus, recusandae rerum beatae iusto libero velit
                    dolorem vel neque reprehenderit obcaecati incidunt, cumque
                    facere sint ex, possimus quam!
                </p>
            </div>
        </div>
    );
};

export default ImplantReview;

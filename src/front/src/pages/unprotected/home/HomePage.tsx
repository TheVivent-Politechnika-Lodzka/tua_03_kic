import ContactBar from "../../../component/ContactBar/ContactBar";
import Slider from "../../../component/Slider/Slider";
import SelectorBar from "../../../component/TopBar/SelectorTopBar/SelectorTopBar";
import styles from "./homepage.module.scss";
import { useTranslation } from "react-i18next";

const HomePage = () => {
  const { t } = useTranslation();

  return (
    <div>
      <div className={styles.slider_section}>
        <Slider />
      </div>

      <div className={styles.content_section}>
        <div className={styles.box_info}>
          <div className={styles.title}>{t("why_us")}</div>
          <div className={styles.second_title}>{t("many_reason")}"</div>
          <div className={styles.items_list}>
            <div className={styles.single_item_circle}>
              <div className={styles.item_image}>
                <img src="brain.jpg" alt="brain.jpg" />
              </div>
              <div className={styles.text_box}>
                <div className={styles.text_square_title}> {t("own_lab")}</div>
                <div className={styles.text_square}>
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book
                </div>
              </div>
            </div>
            <div className={styles.single_item_circle}>
              <div className={styles.item_image}>
                <img src="doctor.jpg" alt="doctor" />
              </div>
              <div className={styles.text_box}>
                <div className={styles.text_square_title}>
                  {t("best_specialist")}
                </div>
                <div className={styles.text_square}>
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book
                </div>
              </div>
            </div>
            <div className={styles.single_item_circle}>
              <div className={styles.item_image}>
                <img src="klient.jpg" alt="klient" />
              </div>
              <div className={styles.text_box}>
                <div className={styles.text_square_title}>
                  {t("satisfied_clients")}
                </div>
                <div className={styles.text_square}>
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className={styles.box_info}>
        <div className={styles.title}> {t("our_clinic")}</div>
        <div className={styles.second_title}>{t("good_location")}</div>
        <div className={styles.items_list}>
          <div className={styles.card}>
            <div className={styles.item_wrapper}>
              <img src="klinika1.jpg" alt="klinika1" />
              <div className={styles.city_name}>KIC Badlandy</div>
              <div className={styles.street_name}>ul. Oak Lawn 204 </div>
            </div>
          </div>
          <div className={styles.card}>
            <div className={styles.item_wrapper}>
              <img src="klinika2.jpg" alt="klinika2" />
              <div className={styles.city_name}>KIC Pacifica</div>
              <div className={styles.street_name}>ul. Racine Avenue 23 </div>
            </div>
          </div>
          <div className={styles.card}>
            <div className={styles.item_wrapper}>
              <img src="klinika3.jpg" alt="klinika3" />
              <div className={styles.city_name}>KIC Westbrook</div>
              <div className={styles.street_name}>ul. Paul 342</div>
            </div>
          </div>
        </div>
      </div>

      <ContactBar />
    </div>
  );
};

export default HomePage;

import ContactBar from "../../../component/ContactBar/ContactBar";
import Slider from "../../../component/Slider";
import TopBar from "../../../component/TopBar/SelectorTopBar";
import "./style.scss";


const MainPage = () => {
  

  return (
    <div>

      <TopBar />
      <div className="slider_section">
        <Slider />
      </div>

      <div className="content_section">
        <div className="box_info">
          <div className="title">DLACZEGO MY?</div>
          <div className="second_title">
            jest wiele powodów a to tylko parę z nich
          </div>
          <div className="items_list">
            <div className="single_item_circle">
              <div className="item_image">
                <img src="brain.jpg" alt="brain.jpg" />
              </div>
              <div className="text_box">
                <div className="text_square_title">Własne laboratorium</div>
                <div className="text_square">
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book
                </div>
              </div>
            </div>
            <div className="single_item_circle">
              <div className="item_image">
                <img src="doctor.jpg" alt="doctor" />
              </div>
              <div className="text_box">
                <div className="text_square_title">
                  Najwyższej klasy specjaliści
                </div>
                <div className="text_square">
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book
                </div>
              </div>
            </div>
            <div className="single_item_circle">
              <div className="item_image">
                <img src="klient.jpg" alt="klient" />
              </div>
              <div className="text_box">
                <div className="text_square_title">
                  Tysiące zadowolonych klietów
                </div>
                <div className="text_square">
                  Lorem Ipsum is simply dummy text of the printing and
                  typesetting industry. Lorem Ipsum has been the industry's
                  standard dummy text ever since the 1500s, when an unknown
                  printer took a galley of type and scrambled it to make a type
                  specimen book{" "}
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="box_info">
        <div className="title">NASZE KLINIKI</div>
        <div className="second_title">
          kliniki w trzech dogodnych lokalizacjach
        </div>
        <div className="items_list">
          <div className="card">
            <div className="item-wrapper">
              <img src="klinika1.jpg" alt="klinika1" />
              <div className="city_name">KIC Badlandy</div>
              <div className="street_name">ul. Oak Lawn 204 </div>
            </div>
          </div>
          <div className="card">
            <div className="item-wrapper">
              <img src="klinika2.jpg" alt="klinika2" />
              <div className="city_name">KIC Pacifica</div>
              <div className="street_name">ul. Racine Avenue 23 </div>
            </div>
          </div>
          <div className="card">
            <div className="item-wrapper">
              <img src="klinika3.jpg" alt="klinika3" />
              <div className="city_name">KIC Westbrook</div>
              <div className="street_name">ul. Paul 342</div>
            </div>
          </div>
        </div>
      </div>

      <ContactBar />
    </div>
  );
};

export default MainPage;

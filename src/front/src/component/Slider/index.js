import "./style.scss";

const Slider = () => {
  const scrolldown = () => {
    const height = document.querySelector("#sliderdiv").offsetHeight;
    window.scrollTo({
      top: height,
      left: 0,
      behavior: "smooth",
    });
  };

  return (
    <div id="sliderdiv">
      <div className="sliderText">
        Kliniczny Instytut Cyberwszczepów
        <div className="scroll_Button" onClick={scrolldown}>
          Dowiedz się więcej
        </div>
      </div>
      <video autoPlay loop muted id="clip" width="100%">
        <source src="main.mp4" type="video/mp4" />
      </video>
    </div>
  );
};

export default Slider;

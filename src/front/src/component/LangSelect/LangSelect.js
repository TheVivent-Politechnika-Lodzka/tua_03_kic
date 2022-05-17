import "./style.scss";
import i18next from "i18next";
import { useState } from "react";

const LangSelect = () => {
  const [value, setValue] = useState();
  return (
    <select
      name="lang"
      id="lang-select"
      onChange={(e) => {
        setValue(e.target.value);
        i18next.changeLanguage(e.target.value);
      }}
      value={value}
    >
      <option value="en">ENG</option>
      <option value="pl">PL</option>
    </select>
  );
};

export default LangSelect;

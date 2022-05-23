import "./langselect.module.scss";
import i18next from "i18next";
import { useState } from "react";
import Cookies from "js-cookie";

import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { useTranslation } from "react-i18next";

const LangSelect = () => {
  const [value, setValue] = useState<string>();
  const {t} = useTranslation();
  return (
    <FormControl focused={true} color="primary">
      <InputLabel id="lang_select">{t("language")}</InputLabel>
      <Select
        labelId="lang-select"
        id="lang-select"
        value={value}
        defaultValue={Cookies.get("i18next")}
        label={t("language")}
        onChange={(e) => {
          setValue(e.target.value);
          i18next.changeLanguage(e.target.value);
        }}
      >
        <MenuItem value="en">ENG</MenuItem>
        <MenuItem value="pl">PL</MenuItem>
      </Select>
    </FormControl>
  );
};

export default LangSelect;

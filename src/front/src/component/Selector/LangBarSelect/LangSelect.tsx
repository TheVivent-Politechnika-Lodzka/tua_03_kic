import styles from "./langselect.module.scss";
import i18next from "i18next";
import { useState } from "react";

import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { useTranslation } from "react-i18next";

const LangSelect = () => {
  const [value, setValue] = useState<string>();
  const { t } = useTranslation();
  return (
    <FormControl focused={true} color="primary" variant="filled">
      <InputLabel id="lang_select">{t("language")}</InputLabel>
      <Select
        labelId="lang-select"
        id="lang-select"
        className={styles.textcolor}
        value={value}
        defaultValue={localStorage.getItem("i18nextLng")}
        label={t("language")}
        onChange={(e) => {
          const lng = e.target.value;
          if (lng) {
            setValue(lng);
            localStorage.setItem("i18nextLng", lng);
            i18next.changeLanguage(lng);
          }
        }}
      >
        <MenuItem value="en">ENG</MenuItem>
        <MenuItem value="pl">PL</MenuItem>
      </Select>
    </FormControl>
  );
};

export default LangSelect;

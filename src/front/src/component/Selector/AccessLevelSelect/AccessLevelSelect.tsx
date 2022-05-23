import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { useStoreSelector } from "../../../redux/reduxHooks";
import { changeLevel } from "../../../redux/userSlice";
import styles from "./accesslevel.module.scss";

import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";
import { useTranslation } from "react-i18next";

const AccessLevelSelect = () => {
  const user = useStoreSelector((state) => state.user);
  const [value, setValue] = useState<string>();
  const dispatch = useDispatch();
  const { t } = useTranslation();

  const changeAccessLevel = (e: any) => {
    e.preventDefault();
    const selected = e.target.value;
    setValue(selected);
    for (let i = 0; i < user.auth.length; i++) {
      if (user.auth[i] === selected) {
        dispatch(
          changeLevel({
            sub: user.sub,
            auth: user.auth,
            index: i,
            exp: user.exp,
          })
        );
      }
    }
  };

  return (
    <FormControl focused={true} color="primary">
      <InputLabel id="accesslevel_select">{t("accessLevel")}</InputLabel>
      <Select
        labelId="accesslevel_select-label"
        id="accesslevel_select"
        className={styles.select}
        value={value}
        defaultValue={user.cur}
        label={t("accessLevel")}
        onChange={changeAccessLevel}
      >
        {user.auth.map((item) => {
          return <MenuItem value={item}>{t(item)}</MenuItem>;
        })}
      </Select>
    </FormControl>
  );
};

export default AccessLevelSelect;

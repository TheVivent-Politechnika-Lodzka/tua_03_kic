import { useState } from "react";
import { useDispatch } from "react-redux";
import { useStoreSelector } from "../../../redux/reduxHooks";
import { changeLevel } from "../../../redux/userSlice";

import InputLabel from "@mui/material/InputLabel";
import MenuItem from "@mui/material/MenuItem";
import FormControl from "@mui/material/FormControl";
import Select from "@mui/material/Select";

const AccessLevelSelect = () => {
  const user = useStoreSelector((state) => state.user);
  const [value, setValue] = useState<string>(user.cur);
  const dispatch = useDispatch();

  const changeAccessLevel = (e: any) => {
    const selected = e.target.value;

    setValue(user.auth[selected]);

    dispatch(
      changeLevel({
        sub: user.sub,
        auth: user.auth,
        index: selected,
        exp: user.exp,
      })
    );
  };

  return (
    <FormControl focused={true} color="primary">
      <InputLabel id="accesslevel_select">Poziom dostępu</InputLabel>
      <Select
        labelId="demo-simple-select-label"
        id="demo-simple-select"
        value={value}
        label="Poziom dostępu"
        onChange={changeAccessLevel}
      >
        {user.auth.map((item, key) => {
          return <MenuItem value={key}>{item}</MenuItem>;
        })}
      </Select>
    </FormControl>
  );
};

export default AccessLevelSelect;

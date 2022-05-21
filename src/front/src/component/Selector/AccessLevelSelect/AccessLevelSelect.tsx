import { useState } from "react";
import { useDispatch } from "react-redux";

import { useStoreSelector } from "../../../redux/reduxHooks";

const AccessLevelSelect = () => {
  const [value, setValue] = useState();
  const user = useStoreSelector((state) => state.user);
  const dispatch = useDispatch();

  const changeAccessLevel = (e: any) => {
    const selected = e.target.value;
  };

  return (
    <select
      name="accesslevel_select"
      id="accesslevel_select"
      onChange={changeAccessLevel}
      defaultValue={user.cur}
      value={value}
    >
      {user.auth.map((item, key) => {
        return (
          <option key={key} value={item}>
            {item}
          </option>
        );
      })}
    </select>
  );
};

export default AccessLevelSelect;

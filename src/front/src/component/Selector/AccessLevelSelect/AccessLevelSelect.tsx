import { useState } from "react";
import { useDispatch } from "react-redux";
import { useStoreSelector } from "../../../redux/reduxHooks";
import { changeLevel } from "../../../redux/userSlice";

const AccessLevelSelect = () => {
  const user = useStoreSelector((state) => state.user);
  const [value, setValue] = useState<string>();
  const dispatch = useDispatch();

  const changeAccessLevel = (e: any) => {
    const selected = e.target.value;
    dispatch(
      changeLevel({
        sub: user.sub,
        auth: user.auth,
        index: selected,
        exp: user.exp,
      })
    );

    setValue(user.auth[selected]);
  };

  return (
    <select
      name="accesslevel_select"
      id="accesslevel_select"
      onChange={changeAccessLevel}
      value={value}
    >
      {user.auth.map((item, key) => {
        return <option value={key}>{item}</option>;
      })}
    </select>
  );
};

export default AccessLevelSelect;

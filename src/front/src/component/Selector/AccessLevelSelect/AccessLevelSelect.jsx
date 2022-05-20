import { useState } from "react";

const AccessLevelSelect = () => {
    const [value, setValue] = useState<string>();
    return (
      <select
        name="accesslevel_select"
        id="accesslevel_select"
        onChange={(e) => {
         console.log("poziom");
        }}
        defaultValue={"ADMINISTARTOR"}
        value={value}
      >
        <option value="ADMINISTARTOR">ADMINISTRATOR</option>
        <option value="SPECIALIST">SPECIALIST</option>
        <option value="CLIENT">CLIENT</option>
      </select>
    );
}

export default AccessLevelSelect;
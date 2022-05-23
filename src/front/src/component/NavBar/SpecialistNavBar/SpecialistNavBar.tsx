import { Fragment } from "react";
import { useNavigate } from "react-router";
import styles from "./appointmentNavBar.module.scss";

import Button from "@mui/material/Button";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import PopupState, { bindTrigger, bindMenu } from "material-ui-popup-state";
import { useTranslation } from "react-i18next";
import { useStoreSelector } from "../../../redux/reduxHooks";

const SpecialistNavBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const user = useStoreSelector((state) => state.user.cur);

  return (
    <PopupState variant="popover" popupId="demo-popup-menu">
      {(popupState) => (
        <Fragment>
          <Button variant="contained" {...bindTrigger(popupState)}>
            {t("specialist")}
          </Button>
          <Menu {...bindMenu(popupState)}>
            {user === "ADMINISTRATOR" ? (
              <MenuItem onClick={() => navigate("/specialist")}>EXAMPLE</MenuItem>
            ) : (
              <></>
            )}
            {user === "SPECIALIST" ? (
              <MenuItem onClick={() => navigate("/specialist")}>EXAMPLE</MenuItem>
            ) : (
              <></>
            )}
          </Menu>
        </Fragment>
      )}
    </PopupState>
  );
};

export default SpecialistNavBar;

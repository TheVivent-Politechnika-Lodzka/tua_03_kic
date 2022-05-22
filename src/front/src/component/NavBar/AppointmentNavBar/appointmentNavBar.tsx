import { Fragment } from "react";
import { useNavigate } from "react-router";
import styles from "./appointmentNavBar.module.scss";

import Button from "@mui/material/Button";
import Menu from "@mui/material/Menu";
import MenuItem from "@mui/material/MenuItem";
import PopupState, { bindTrigger, bindMenu } from "material-ui-popup-state";
import { useTranslation } from "react-i18next";
import { useStoreSelector } from "../../../redux/reduxHooks";

const AppointmentNavBar = () => {
  const navigate = useNavigate();
  const { t } = useTranslation();
  const user = useStoreSelector((state) => state.user.cur);

  return (
    <PopupState variant="popover" popupId="demo-popup-menu">
      {(popupState) => (
        <Fragment>
          <Button variant="contained" {...bindTrigger(popupState)}>
            {t("appointment")}
          </Button>
          <Menu {...bindMenu(popupState)}>
            {user === "ADMINISTRATOR" ? (
              <MenuItem onClick={() => navigate("/specialist")}>PAGE1</MenuItem>
            ) : (
              <></>
            )}
          </Menu>
        </Fragment>
      )}
    </PopupState>
  );
};

export default AppointmentNavBar;

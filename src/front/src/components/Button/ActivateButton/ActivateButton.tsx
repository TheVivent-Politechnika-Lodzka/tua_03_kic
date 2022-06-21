import "./Activatebutton.module.sass";
import { useTranslation } from "react-i18next";
import { useActivateAccountMutation } from "../../../api/api";
import { Button } from "react-bootstrap";

interface ActivateProps {
    login: string;
    ETag: string;
}

const ActivateButton = ({ ETag, login }: ActivateProps) => {
    const [change, { isLoading }] = useActivateAccountMutation();

    const { t } = useTranslation();

    const handleSubmit = async (event: any) => {
        event.preventDefault();
        const res = await change({
            login,
            tag: { ETag },
        });
    };

    return <Button onClick={handleSubmit}>{t("active")}</Button>;
};

export default ActivateButton;

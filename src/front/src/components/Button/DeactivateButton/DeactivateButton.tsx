import { Button } from "react-bootstrap";
import { useTranslation } from "react-i18next";
import { useDeactivateAccountMutation } from "../../../api/api";

interface ActivateProps {
    login: string;
    ETag : string;
}

export const DeactivateButton = ({ETag, login}: ActivateProps) => {
    const [change, { isLoading }] = useDeactivateAccountMutation();

    const {t} = useTranslation();

    const handleSubmit = async (event: any) => {
        event.preventDefault();
        const res = await change({
            login,
            tag: {ETag}
        });
    };

    return (
        <Button onClick = {handleSubmit}>
            {t("deactive")}
        </Button>
    );
};



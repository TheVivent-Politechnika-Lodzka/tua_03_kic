import style from "./editImplantPage.module.scss";
import { faCancel, faCheck } from "@fortawesome/free-solid-svg-icons";
import { showNotification } from "@mantine/notifications";
import { useContext, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import ActionButton from "../../../../components/shared/ActionButton/ActionButton";
import InputWithValidation from "../../../../components/shared/InputWithValidation/InputWithValidation";
import ValidationMessage from "../../../../components/shared/ValidationMessage/ValidationMessage";
import { validationContext } from "../../../../context/validationContext";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";
import { useTranslation } from "react-i18next";
import { Center, Image } from "@mantine/core";
import { HiOutlinePhotograph } from "react-icons/hi";
import { uploadPhoto } from "../../../../utils/upload";
import { editImplant, EditImplantResponse, getImplant, GetImplantResponse } from "../../../../api/mop";

import ConfirmActionModal from "../../../../components/shared/ConfirmActionModal/ConfirmActionModal";

export const EditImplantPage = () => {
    const { t } = useTranslation();
    const [loading, setLoading] = useState<Loading>({
        pageLoading: true,
        actionLoading: false,
    });

    const [opened, setOpened] = useState<boolean>(false);
    const [implant, setImplant] = useState<GetImplantResponse>();
    const { id } = useParams();

    const {
        state,
        state: {
            isImplantNameValid,
            isManufacturerValid,
            isPriceValid,
            isDurationValid,
            isDescriptionValid,
        },
        dispatch,
    } = useContext(validationContext);

    useEffect(() => {
        handleGetImplant();
    }, []);

    const navigate = useNavigate();

    const isEveryFieldValid =
        isImplantNameValid &&
        isManufacturerValid &&
        isPriceValid &&
        isDurationValid &&
        isDescriptionValid;

    const handleGetImplant = async () => {
        if (!id) return;
        setLoading({ ...loading, pageLoading: true });
        const response = await getImplant(id);
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            setLoading({ ...loading, pageLoading: false });
            return;
        }
        setImplant({...response, duration: Math.round(response.duration /60)});
        setLoading({ ...loading, pageLoading: false });

    };

    const handleSubmit = async () => {
        if (!implant || !id) return;
        const response = await editImplant(id, {...implant, duration: implant.duration*60});
        if ("errorMessage" in response) {
            showNotification(failureNotificationItems(response.errorMessage));
            return;
        }
        showNotification(
            successNotficiationItems(t("editImplantPage.implantEdited"))
        );
        navigate("/implants");
    };

    return (
        <section className={style.edit_implant_page}>
            <div className={style.edit_implant_page_header}>
                <h2> {t("editImplantPage.addImplant")}</h2>
            </div>
            <div className={style.edit_implant_page_content}>
                <div className={style.edit_data_account_wrapper}>
                    <div className={style.edit_fields_wrapper}>
                        
                        { implant?.image.length === 0 ? (
                            <div className={`${style.image} ${style.margin}`}>
                                <Center>
                                    <HiOutlinePhotograph size="80px" />
                                </Center>
                            </div>
                        ) : (
                            <Image
                                radius="md"
                                src={implant?.image}
                                height="20vw"
                                alt="image create"
                                styles={{
                                    root: { marginTop: "6vh" },
                                }}
                            />
                        )}

                        <input
                            id="file-input"
                            type="file"
                            onChange={async (event) => {
                                const u = await uploadPhoto(event);
                                if (u) {
                                    if (implant)
                                    setImplant({
                                        ...implant,
                                        image: u,
                                    });
                                }
                            }}
                        />
                    </div>
                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("editImplantPage.name")}
                                value={implant?.name}
                                validationType="VALIDATE_IMPLANT_NAME"
                                isValid={isImplantNameValid}
                                onChange={(e) => {
                                    if (implant)
                                    setImplant({
                                        ...implant,
                                        name: e.target.value,
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isImplantNameValid}
                                message={t("editImplantPage.nameMsg")}
                            />
                        </div>
                    </div>
                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("editImplantPage.manufacturer")}
                                value={implant?.manufacturer}
                                validationType="VALIDATE_MANUFACTURER"
                                isValid={isManufacturerValid}
                                onChange={(e) => {
                                    if (implant)
                                    setImplant({
                                        ...implant,
                                        manufacturer: e.target.value,
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isManufacturerValid}
                                message={t("editImplantPage.manufacturerMsg")}
                            />
                        </div>
                    </div>

                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("editImplantPage.price")}
                                value={implant?.price.toString() }
                                validationType="VALIDATE_PRICE"
                                isValid={isPriceValid}
                                type="number"
                                onChange={(e) => {
                                    if (implant)
                                    setImplant({
                                        ...implant,
                                        price: parseInt(e.target.value),
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isPriceValid}
                                message={t("editImplantPage.priceMsg")}
                            />
                        </div>
                    </div>

                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("editImplantPage.duration")}
                                value={implant?.duration.toString()}
                                validationType="VALIDATE_DURATION"
                                isValid={isDurationValid}
                                type="number"
                                onChange={(e) => {
                                    if (implant)
                                    setImplant({
                                        ...implant,
                                        duration: parseInt(e.target.value),
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isDurationValid}
                                message={t("editImplantPage.durationMsg")}
                            />
                        </div>
                    </div>

                    <div className={style.edit_fields_wrapper}>
                        <div className={style.edit_field}>
                            <InputWithValidation
                                title={t("editImplantPage.description")}
                                value={implant?.description}
                                validationType="VALIDATE_DESCRIPTION"
                                isValid={isDescriptionValid}
                                onChange={(e) => {
                                    if (implant)
                                    setImplant({
                                        ...implant,
                                        description: e.target.value,
                                    });
                                }}
                                required
                            />
                            <ValidationMessage
                                isValid={isDescriptionValid}
                                message={t("editImplantPage.descriptionMsg")}
                            />
                        </div>
                    </div>

                    <div className={style.edit_implant_buttons_wrapper}>
                        <ActionButton
                            onClick={() => {
                                setOpened(true);
                            }}
                            isDisabled={!isEveryFieldValid}
                            icon={faCheck}
                            color="green"
                            title={t("editImplantPage.confirm")}
                        />
                        <ActionButton
                            onClick={() => {
                                navigate("/implants");
                            }}
                            icon={faCancel}
                            color="red"
                            title={t("editImplantPage.cancel")}
                        />
                    </div>
                </div>
            </div>

            <ConfirmActionModal
                isOpened={opened}
                onClose={() => {
                    setOpened(false);
                }}
                handleFunction={async () => {
                    setOpened(false);
                    handleSubmit();
                }}
                isLoading={loading.actionLoading as boolean}
                title={t("editImplantPage.modalTitle")}
            >
                {t("editImplantPage.confirmMsg")}
            </ConfirmActionModal>
        </section>
    );
};

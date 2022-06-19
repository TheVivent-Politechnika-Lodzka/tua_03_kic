import styles from "./createImplantPage.module.scss";
import {
    Button,
    Center,
    Grid,
    Group,
    Image,
    Input,
    NumberInput,
    Textarea,
    TextInput,
} from "@mantine/core";
import { Container } from "react-bootstrap";
import { Box } from "@mui/material";
import { useForm } from "@mantine/form";
import { useTranslation } from "react-i18next";
import { createImplant } from "../../../../api/mop";
import { uploadPhoto } from "./upload";
import { useState } from "react";
import { HiOutlinePhotograph } from "react-icons/hi";
import { showNotification } from "@mantine/notifications";
import {
    failureNotificationItems,
    successNotficiationItems,
} from "../../../../utils/showNotificationsItems";

export const CreateImplantPage = () => {
    const { t } = useTranslation();
    const [url, setUrl] = useState(``);
    const form = useForm<{
        name: string;
        manufacturer: string;
        price: number | undefined;
        duration: number | undefined;
        description: string;
    }>({
        initialValues: {
            name: "",
            manufacturer: "",
            price: undefined,
            duration: undefined,
            description: "",
        },
        validate: (values) => ({
            name:
                values.name.length < 4
                    ? `${t("createImplantPage.tooShort")}`
                    : values.name.length > 50
                    ? `${t("createImplantPage.tooLong")}`
                    : null,
            manufacturer:
                values.manufacturer.length < 10
                    ? `${t("createImplantPage.tooShort")}`
                    : values.manufacturer.length > 50
                    ? `${t("createImplantPage.tooLong")}`
                    : null,
            price:
                values.price === undefined
                    ? `${t("createImplantPage.priceReq")}`
                    : values.price <= 0
                    ? `${t("createImplantPage.pricePos")}`
                    : null,
            duration:
                values.duration === undefined
                    ? `${t("createImplantPage.durationReq")}`
                    : values.duration <= 0
                    ? `${t("createImplantPage.durationPos")}`
                    : null,
            description:
                values.description.length < 100
                    ? `${t("createImplantPage.tooShort")}`
                    : values.description.length > 1000
                    ? `${t("createImplantPage.tooLong")}`
                    : null,
        }),
    });

    const putImplant = (values: any) => {
        try {
            createImplant({
                name: values.name,
                description: values.description,
                manufacturer: values.manufacturer,
                price: values.price,
                duration: values.duration,
                url: url,
            });
            showNotification(successNotficiationItems(""));
        } catch (error: ApiError | any) {
            showNotification(failureNotificationItems(error?.errorMessage));
        }
    };

    return (
        <div>
            <Center>
                <div className={styles.text}>
                    {t("createImplantPage.addImplant")}
                </div>
            </Center>
            <Center>
                <div className={styles.container}>
                    <Container fluid={true}>
                        <Grid>
                            <Grid.Col
                                span={6}
                                sx={{ width: "35vw", height: "60vh" }}
                            >
                                <Center>
                                    {url.length === 0 ? (
                                        <div
                                            className={`${styles.image} ${styles.margin}`}
                                        >
                                            <Center>
                                                <HiOutlinePhotograph size="80px" />
                                            </Center>
                                        </div>
                                    ) : (
                                        <Image
                                            radius="md"
                                            src={url}
                                            height="20vw"
                                            alt="image create"
                                            styles={{
                                                root: { marginTop: "6vh" },
                                            }}
                                        />
                                    )}
                                </Center>
                                <Center>
                                    <input
                                        id="file-input"
                                        type="file"
                                        onChange={async (event) => {
                                            const u = await uploadPhoto(event);
                                            if (u) {
                                                setUrl(u);
                                            }
                                        }}
                                    />
                                </Center>
                            </Grid.Col>
                            <Grid.Col
                                span={6}
                                sx={{ width: "35vw", height: "60vh" }}
                            >
                                <Center>
                                    <div className={styles.margin}>
                                        <Box
                                            sx={{
                                                width: "35vw",
                                                height: "50vh",
                                            }}
                                            mx="auto"
                                        >
                                            <form
                                                onSubmit={form.onSubmit(
                                                    (values) => {
                                                        putImplant(values);
                                                    }
                                                )}
                                            >
                                                <TextInput
                                                    label={t(
                                                        "createImplantPage.name"
                                                    )}
                                                    classNames={{
                                                        wrapper: `${styles.inputMargin}`,
                                                        input: `${styles.formfield}`,
                                                        label: `${styles.formlabel}`,
                                                    }}
                                                    placeholder={t(
                                                        "createImplantPage.name"
                                                    )}
                                                    {...form.getInputProps(
                                                        "name"
                                                    )}
                                                    variant="unstyled"
                                                    required
                                                />
                                                <TextInput
                                                    label={t(
                                                        "createImplantPage.manufacturer"
                                                    )}
                                                    placeholder={t(
                                                        "createImplantPage.manufacturer"
                                                    )}
                                                    classNames={{
                                                        wrapper: `${styles.inputMargin}`,
                                                        input: `${styles.formfield}`,
                                                        label: `${styles.formlabel}`,
                                                    }}
                                                    {...form.getInputProps(
                                                        "manufacturer"
                                                    )}
                                                    variant="unstyled"
                                                    required
                                                />
                                                <Center inline>
                                                    <Center inline>
                                                        <NumberInput
                                                            mt="sm"
                                                            label={t(
                                                                "createImplantPage.price"
                                                            )}
                                                            placeholder="000 ZŁ"
                                                            classNames={{
                                                                wrapper: `${styles.inputMargin}`,
                                                                input: `${styles.numberfield}`,
                                                                label: `${styles.formlabel}`,
                                                            }}
                                                            {...form.getInputProps(
                                                                "price"
                                                            )}
                                                            variant="unstyled"
                                                            required
                                                        />
                                                    </Center>

                                                    <NumberInput
                                                        mt="sm"
                                                        label={t(
                                                            "createImplantPage.duration"
                                                        )}
                                                        placeholder="000 MIN"
                                                        classNames={{
                                                            wrapper: `${styles.inputMargin}`,
                                                            input: `${styles.numberfield}`,
                                                            label: `${styles.formlabel}`,
                                                        }}
                                                        {...form.getInputProps(
                                                            "duration"
                                                        )}
                                                        variant="unstyled"
                                                        required
                                                    />
                                                </Center>

                                                <Textarea
                                                    label={t(
                                                        "createImplantPage.description"
                                                    )}
                                                    placeholder={t(
                                                        "createImplantPage.description"
                                                    )}
                                                    {...form.getInputProps(
                                                        "description"
                                                    )}
                                                    classNames={{
                                                        wrapper: `${styles.inputMargin}`,
                                                        input: `${styles.descriptionfield}`,
                                                        label: `${styles.formlabel}`,
                                                    }}
                                                    variant="unstyled"
                                                    required
                                                />
                                                <Group position="left" mt="md">
                                                    <Button
                                                        type="submit"
                                                        variant="gradient"
                                                        gradient={{
                                                            from: "teal",
                                                            to: "lime",
                                                            deg: 105,
                                                        }}
                                                        styles={{
                                                            root: {
                                                                marginTop:
                                                                    "2vh",
                                                                minWidth:
                                                                    "15vw",
                                                                height: "5vh",
                                                            },
                                                        }}
                                                    >
                                                        {t(
                                                            "createImplantPage.addImplantUpper"
                                                        )}
                                                    </Button>
                                                </Group>
                                            </form>
                                        </Box>
                                    </div>
                                </Center>
                            </Grid.Col>
                        </Grid>
                    </Container>
                </div>
            </Center>
        </div>
    );
};

import styles from "./createImplantPage.module.scss";
import {
    Button,
    Center,
    Grid,
    Group,
    NumberInput,
    Textarea,
    TextInput,
} from "@mantine/core";
import { Container } from "react-bootstrap";
import { GreenGradientButton } from "../../../../components/Button/GreenGradientButton";
import { BlueGradientButton } from "../../../../components/Button/BlueGradientButton copy";
import { Box } from "@mui/material";
import { useForm } from "@mantine/form";

export const CreateImplantPage = () => {
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
                    ? "Too short name"
                    : values.name.length > 50
                    ? "Too long name"
                    : null,
            manufacturer:
                values.manufacturer.length < 10
                    ? "Too short name"
                    : values.manufacturer.length > 50
                    ? "Too long name"
                    : null,
            price:
                values.price === undefined
                    ? "Price is required"
                    : values.price < 1
                    ? "Price must be positive number"
                    : null,
            duration:
                values.duration === undefined
                    ? "Duration time is required"
                    : values.duration < 1
                    ? "Duration must be positive number"
                    : null,
            description:
                values.description.length < 100
                    ? "Too short description"
                    : values.description.length > 1000
                    ? "Too long description"
                    : null,
        }),
    });

    return (
        <div>
            <Center>
                <div className={styles.text}>Dodaj wszczep</div>
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
                                    <div
                                        className={`${styles.image} ${styles.margin}`}
                                    ></div>
                                </Center>
                                <Center>
                                    <div className={styles.margin}>
                                        <BlueGradientButton
                                            label="DODAJ ZDJECIE"
                                            onClick={() => console.log("D")}
                                        />
                                    </div>
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
                                                    (values: any) =>
                                                        console.log(values)
                                                )}
                                            >
                                                <TextInput
                                                    label="Name"
                                                    classNames={{
                                                        wrapper: `${styles.inputMargin}`,
                                                        input: `${styles.formfield}`,
                                                        label: `${styles.formlabel}`,
                                                    }}
                                                    placeholder="Name"
                                                    {...form.getInputProps(
                                                        "name"
                                                    )}
                                                    variant="unstyled"
                                                    required
                                                />
                                                <TextInput
                                                    label="Manufacturer"
                                                    placeholder="Manufacturer"
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
                                                            label="Price"
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
                                                        label="Duration"
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
                                                    label="Description"
                                                    placeholder="Description"
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
                                                <Group
                                                    position="center"
                                                    mt="md"
                                                >
                                                    <Button type="submit">
                                                        Submit
                                                    </Button>
                                                </Group>
                                            </form>
                                        </Box>
                                    </div>
                                </Center>
                            </Grid.Col>
                        </Grid>
                        <Center>
                            <div className={styles.margin}>
                                <GreenGradientButton
                                    label="DODAJ WSZCZEP"
                                    onClick={() => console.log("heheh")}
                                />
                            </div>
                        </Center>
                    </Container>
                </div>
            </Center>
        </div>
    );
};

import {
    Center,
    Container,
    Grid,
    Input,
    Pagination,
    Select,
} from "@mantine/core";
import { useEffect, useState } from "react";
import {
    AppointmentListResponse,
    AppointmentListElementDto,
    listAppointments,
} from "../../../../api/mop";
import { AppointmentListElement } from "../../../../components/AppointmentListElement";
import { FaSearch } from "react-icons/fa";
import { useTranslation } from "react-i18next";
import { useStoreSelector } from "../../../../redux/reduxHooks";

export const AppointmentListPage = () => {
    const [phrase, setPhrase] = useState<string>("");
    const [amountElement, setAmountElement] = useState<string | null>("1");
    const [appointmentList, setAppointmentList] =
        useState<AppointmentListResponse>({
            totalCounts: 0,
            totalPages: 0,
            currentPage: 0,
            data: [],
        });
    const [page, setPage] = useState<number>(1);
    const { t } = useTranslation();
    const user = useStoreSelector((state) => state.user.cur);

    const amountSelectList = [
        {
            label: `1 ${t("appointmentListPage.items")}`,
            value: "1",
        },
        {
            label: `2 ${t("appointmentListPage.items")}`,
            value: "2",
        },
        { label: `3 ${t("appointmentListPage.items")}`, value: "3" },
    ];

    const fetchData = async () => {
        let data;
        try {
            if (amountElement !== null) {
                data = await listAppointments({
                    page: page,
                    size: JSON.parse(amountElement),
                    phrase: phrase,
                });
            }
        } catch (err) {
            alert(err);
        }

        const check = (value: any): value is AppointmentListResponse => {
            return true;
        };

        if (check(data)) setAppointmentList(data);
    };

    useEffect(() => {
        fetchData();
    }, [page, amountElement]);

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            fetchData();
        }, 600);

        return () => clearTimeout(delayDebounceFn);
    }, [phrase]);

    return (
        <Container fluid={true} mt={40}>
            <Grid mt="xs">
                <Grid.Col span={6} offset={3}>
                    <Input
                        className="search"
                        icon={<FaSearch size={"26px"} />}
                        placeholder={t("appointmentListPage.search")}
                        value={phrase}
                        onChange={(e: any) => setPhrase(e.target.value)}
                        styles={{
                            defaultVariant: {
                                backgroundColor: "#262633",
                                color: "#737373",
                                fontSize: "2rem",
                                fontWeight: "bold",
                                marginLeft: "1rem",
                            },
                            input: {
                                color: "#737373",
                                fontSize: "2rem",
                                height: "80%",
                                borderRadius: "1rem",
                            },
                            icon: {
                                marginLeft: "1rem",
                                height: "80%",
                            },
                            wrapper: { height: "100%" },
                        }}
                    />
                </Grid.Col>
                <Grid.Col span={2} offset={1}>
                    <Select
                        label={t("appointmentListPage.amount")}
                        data={amountSelectList}
                        value={amountElement}
                        onChange={setAmountElement}
                        styles={{
                            defaultVariant: {
                                backgroundColor: "#262633",
                                borderRadius: "10px",
                                color: "#737373",
                                fontSize: "1.5vw",
                            },

                            label: { color: "#737373", fontSize: "13px" },
                            input: {
                                color: "#737373",
                                fontSize: "1.5vw",
                                height: "100%",
                            },
                        }}
                    />
                </Grid.Col>
            </Grid>

            <Grid mt={40}>
                {appointmentList?.data.map(
                    (item: AppointmentListElementDto, index: number) => (
                        <Grid.Col key={index} span={10} offset={1}>
                            <AppointmentListElement element={item} />
                        </Grid.Col>
                    )
                )}
            </Grid>
            <Container fluid={true}>
                <Center
                    inline
                    style={{
                        marginTop: "5vh",
                        marginLeft: "auto",
                        marginRight: "auto",
                        width: "100%",
                    }}
                >
                    <Pagination
                        total={appointmentList.totalPages}
                        page={page}
                        onChange={setPage}
                    />
                </Center>
            </Container>
        </Container>
    );
};

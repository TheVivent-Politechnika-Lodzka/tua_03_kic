import { Center, Container, Grid, Input, Select } from "@mantine/core";
import { useEffect, useState } from "react";
import { ListElement } from "../../../components/ListElement";
import { FaSearch } from "react-icons/fa";
import { useTranslation } from "react-i18next";
import { useStoreSelector } from "../../../redux/reduxHooks";
import { useNavigate } from "react-router";
import ActionButton from "../../../components/shared/ActionButton/ActionButton";
import { faCheck } from "@fortawesome/free-solid-svg-icons";
import { ListImplantResponse, listImplants } from "../../../api";
import Pagination from "../../../components/Pagination/Pagination";

export const ImplantListPage = () => {
    const [phrase, setPhrase] = useState<string>("");
    const [amountElement, setAmountElement] = useState<string | null>("5");
    const [status, setStatus] = useState<string | null>("false");
    const [implantList, setImplantList] = useState<ListImplantResponse>({
        pageSize: 0,
        totalPages: 0,
        currentPage: 0,
        data: [],
    });
    const { t } = useTranslation();
    const user = useStoreSelector((state) => state.user.cur);
    const navigate = useNavigate();

    const [pagination, setPagination] = useState<Pagination>({
        currentPage: 1,
        pageSize: 1,
        totalPages: 1,
    });

    const statusSelectList = [
        {
            label: `${t("implantListPage.active")}`,
            value: "false",
        },
        {
            label: `${t("implantListPage.archived")}`,
            value: "true",
        },
    ];

    const amountSelectList = [
        {
            label: `5 ${t("implantListPage.items")}`,
            value: "5",
        },
        {
            label: `10 ${t("implantListPage.items")}`,
            value: "10",
        },
        { label: `15 ${t("implantListPage.items")}`, value: "15" },
    ];

    const fetchData = async () => {
        let data;
        try {
            if (
                amountElement !== null &&
                pagination.currentPage !== undefined
            ) {
                data = await listImplants({
                    page: pagination.currentPage,
                    size: JSON.parse(amountElement),
                    phrase: phrase,
                    archived: status === "true",
                });
            }
        } catch (err) {
            alert(err);
        }

        const check = (value: any): value is ListImplantResponse => {
            return true;
        };

        if (check(data)) {
            setImplantList(data);
            setPagination({
                currentPage: data.currentPage,
                pageSize: data.pageSize,
                totalPages: data.totalPages,
            });
        }
    };

    useEffect(() => {
        fetchData();
    }, [amountElement, status, pagination.currentPage]);

    useEffect(() => {
        const delayDebounceFn = setTimeout(() => {
            fetchData();
        }, 600);

        return () => clearTimeout(delayDebounceFn);
    }, [phrase]);

    return (
        <Container fluid={true} mt={40}>
            <Grid>
                <Grid.Col span={2}>
                    {user === "ADMINISTRATOR" ? (
                        <ActionButton
                            onClick={() => navigate("/create-implant")}
                            title={t("implantListPage.addImplant")}
                            color="green"
                            icon={faCheck}
                        />
                    ) : (
                        <></>
                    )}
                </Grid.Col>
                <Grid.Col span={2} offset={8}>
                    <Select
                        label={t("implantListPage.status")}
                        data={statusSelectList}
                        value={status}
                        onChange={setStatus}
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

            <Grid mt="xs">
                <Grid.Col span={6} offset={3}>
                    <Input
                        className="search"
                        // icon={<FaSearch size={"26px"} />}
                        placeholder={t("implantListPage.search")}
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
                                zIndex: 0,
                            },
                            wrapper: { height: "100%" },
                        }}
                    />
                </Grid.Col>
                <Grid.Col span={2} offset={1}>
                    <Select
                        label={t("implantListPage.amount")}
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
                {implantList?.data.map((item: ImplantDto, index: number) => (
                    <Grid.Col key={index} span={10} offset={1}>
                        <ListElement element={item} />
                    </Grid.Col>
                ))}
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
                        pagination={pagination}
                        handleFunction={setPagination}
                    />
                </Center>
            </Container>
        </Container>
    );
};

import axios from "axios";

axios.defaults.baseURL = "https://localhost:9000/api";
// axios.defaults.baseURL = "https://kic.agency:8403/api";
axios.defaults.headers.post["Content-Type"] = "application/json";
// auto-logowanie
axios.interceptors.request.use((config) => {
    const token = localStorage.getItem("ACCESS_TOKEN");
    if (token && token.length !== 0) {
        config.headers = config.headers ?? {};
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});
// dodawanie języka
axios.interceptors.request.use((config) => {
    // const lang = navigator.language;
    const lang = localStorage.getItem("i18nextLng"); // zakładam, że to będzie preferowane
    config.headers = config.headers ?? {};
    config.headers["Accept-Language"] = lang ?? "pl";
    return config;
});
// usunięcie cudzysłowia z If-Match
axios.interceptors.request.use((config) => {
    if (!config.headers) return config;

    const ifmatch = config.headers["If-Match"];
    if (typeof ifmatch !== "string") return config;

    config.headers["If-Match"] = ifmatch.replace(/"/g, "");
    return config;
});

export * from "./auth";
export * from "./mok";
export * from "./mop";

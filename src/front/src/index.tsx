import React from "react";
import ReactDOM from "react-dom/client";
import "./index.scss";
import reportWebVitals from "./reportWebVitals";
import { Provider } from "react-redux";
import store from "./redux/store";
import "./i18n";
import App from "./App";
import { MantineProvider } from "@mantine/core";
import { NotificationsProvider } from "@mantine/notifications";

const container = document.getElementById("root");
const root = ReactDOM.createRoot(container!);

root.render(
    <React.StrictMode>
        <MantineProvider>
            <NotificationsProvider>
                <Provider store={store}>
                    <App />
                </Provider>
            </NotificationsProvider>
        </MantineProvider>
    </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();

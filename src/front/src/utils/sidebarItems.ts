import {
    faCalendarAlt,
    faHome,
    faMicrochip,
    faStethoscope,
    faUser,
    faUserCog,
    IconDefinition,
} from "@fortawesome/free-solid-svg-icons";


interface SideBarRoute {
    title: string;
    icon: IconDefinition;
    path: string;
}

const unauthenticatedSidebarItems: SideBarRoute[] = [
    { title: "title.main_page", icon: faHome, path: "/" },
    { title: "title.specialist", icon: faStethoscope, path: "/specialists" },
    { title: "title.implants", icon: faMicrochip, path: "/implants" },
];

const authorizedSidebarItems: SideBarRoute[] = [
    { title: "title.main_page", icon: faHome, path: "/" },
    { title: "title.profile", icon: faUser, path: "/account" },
    { title: "title.visits", icon: faCalendarAlt, path: "/visits" },
    { title: "title.specialist", icon: faStethoscope, path: "/specialists" },
    { title: "title.implants", icon: faMicrochip, path: "/implants" },
];

const adminSidebarItems: SideBarRoute[] = [
    { title: "title.main_page", icon: faHome, path: "/" },
    { title: "title.profile", icon: faUser, path: "/account" },
    { title: "title.manage.profile", icon: faUserCog, path: "/accounts" },
    { title: "title.manage.visits", icon: faCalendarAlt, path: "/visits" },
    {
        title: "title.specialist",
        icon: faStethoscope,
        path: "/specialists",
    },
    {
        title: "title.manage.implants",
        icon: faMicrochip,
        path: "/implants",
    },
];

const handleSwitchSidebarItems = (accessLevel: AccessLevelType) => {
    switch (accessLevel) {
        case "ADMINISTRATOR": {
            return adminSidebarItems;
        }
        case "CLIENT":
        case "SPECIALIST": {
            return authorizedSidebarItems;
        }
        case "UNAUTHORIZED": {
            return unauthenticatedSidebarItems;
        }
        default: {
            return unauthenticatedSidebarItems;
        }
    }
};

export { handleSwitchSidebarItems };

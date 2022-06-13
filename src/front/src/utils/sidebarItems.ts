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
    { title: "Home", icon: faHome, path: "/" },
    { title: "Specjaliści", icon: faStethoscope, path: "/specialists" },
    { title: "Cyberwszczepy", icon: faMicrochip, path: "/chips" },
];

const authorizedSidebarItems: SideBarRoute[] = [
    { title: "Strona główna", icon: faHome, path: "/" },
    { title: "Profil użytkownika", icon: faUser, path: "/account" },
    { title: "Wizyty", icon: faCalendarAlt, path: "/visits" },
    { title: "Specjaliści", icon: faStethoscope, path: "/specialists" },
    { title: "Cyberwszczepy", icon: faMicrochip, path: "/chips" },
];

const adminSidebarItems: SideBarRoute[] = [
    { title: "Strona główna", icon: faHome, path: "/" },
    { title: "Profil użytkownika", icon: faUser, path: "/account" },
    { title: "Zarządzaj kontami", icon: faUserCog, path: "/accounts" },
    { title: "Zarządzaj wizytami", icon: faCalendarAlt, path: "/visits" },
    {
        title: "Zarządzaj specjalistami",
        icon: faStethoscope,
        path: "/specialists",
    },
    { title: "Zarządzaj cyberwszczepami", icon: faMicrochip, path: "/chips" },
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

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


const adminSidebarItems: SideBarRoute[] = [
    { title: "Strona główna", icon: faHome, path: "/" },
    { title: "Profil użytkownika", icon: faUser, path: "/account" },
    { title: "Zarządzaj kontami", icon: faUserCog, path: "/accounts" },
    { title: "Zarządzaj wizytami", icon: faCalendarAlt, path: "/visits" },
    { title: "Zarządzaj specjalistami", icon: faStethoscope, path: "/specialists" },
    { title: "Zarządzaj cyberwszczepami", icon: faMicrochip, path: "/chips" },
];

const sidebarItems: SideBarRoute[] = [
    { title: "Strona główna", icon: faHome, path: "/" },
    { title: "Profil użytkownika", icon: faUser, path: "/account" },
    { title: "Wizyty", icon: faCalendarAlt, path: "/visits" },
    { title: "Specjaliści", icon: faStethoscope, path: "/specialists" },
    { title: "Cyberwszczepy", icon: faMicrochip, path: "/chips" },
];

const handleSwitchSidebarItems = (isAdmin: boolean) => {
    return isAdmin ? adminSidebarItems : sidebarItems;
}



export { handleSwitchSidebarItems };

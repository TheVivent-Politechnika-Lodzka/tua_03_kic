interface ApiError {
    errorMessage: string;
    status: number;
}

interface Taggable {
    id: string;
    version: number;
}

interface Credentials {
    login: string;
    password: string;
}

type AccessLevelType =
    | "ADMINISTRATOR"
    | "CLIENT"
    | "SPECIALIST"
    | "UNAUTHORIZED";

interface AccessLevel {
    level: AccessLevelType;
    contactEmail?: string;
    phoneNumber?: string;
    pesel?: string;
}

interface AccountDetails extends Taggable {
    login: string;
    firstName: string;
    lastName: string;
    email: string;
    phoneNumber: string;
    pesel: string;
    language: {
        language: string;
    };
    active: boolean;
    confirmed: boolean;
    url?: string;
    accessLevels: AccessLevel[];
}

interface Etag {
    etag: string;
}

interface Captcha {
    captcha: string;
}

interface Loading {
    pageLoading: boolean;
    actionLoading?: boolean;
}

interface Pagination {
    currentPage?: number;
    pageSize?: number;
    totalPages?: number;
}

type Status =
    | "PENDING"
    | "REJECTED"
    | "ACCEPTED"
    | "FINISHED";

interface ConfirmActionModal {
    title: string;
    message: string;
}

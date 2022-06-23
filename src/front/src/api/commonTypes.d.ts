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

interface ImplantDto extends Taggable {
    name: string;
    description: string;
    manufacturer: string;
    price: number;
    archived: boolean;
    popularity: number;
    duration: number;
    image: string;
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
    url: string;
}

interface ImplantReview {
    id: string;
    clientLogin: string;
    createdAt: string;
    implantId: string;
    rating: number;
    review: string;
    clientImage: string;
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
    currentPage: number;
    pageSize: number;
    totalPages: number;
}

type Status = "PENDING" | "REJECTED" | "ACCEPTED" | "FINISHED";

interface ConfirmActionModal {
    title: string;
    message: string;
}

interface AppointmentDto extends Taggable {
    client: AccountDetails;
    specialist: AccountDetails;
    implant: ImplantDto;
    startDate: string;
    endDate: string;
    price: number;
    description: string;
    status: Status;
    appointmentImplant: ImplantDto;
}

interface CreateImplantDto {
    name: string;
    description: string;
    manufacturer: string;
    price: number;
    duration: number;
    url: string;
}

interface PaginationRequest {
    page: number;
    size: number;
    phrase?: string;
}

interface AppointmentListElementDto {
    id: string;
    client: AccountDetails;
    specialist: AccountDetails;
    implant: ImplantDto;
    status: Status;
    startDate: string;
    endDate: string;
    description: string;
    price: number;
    appointmentImplant: ImplantDto;
}

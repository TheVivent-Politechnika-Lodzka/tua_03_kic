export type AccessLevelName = "ADMINISTRATOR" | "CLIENT" | "SPECIALIST" | "UNAUTHORIZED";

export interface Language {
    language: string;
}

export interface AbstractDto {
    ETag: string;
    createdAt: string;
    lastModified: string;
}

export interface JWT {
    auth: string;
    sub: string;
    exp: number;
}

export interface JWTTokens {
    accessToken: JWT;
    refreshToken: JWT;
}

interface ShowAccountInfo {
    login: string;
    firstName: string;
    lastName: string;
    accessLevels: string[];
    email: string;
    active: boolean;
    ETag: string;
}

export interface PaginationData {
    totalCount: number;
    totalPages: number;
    currentPage: number;
}

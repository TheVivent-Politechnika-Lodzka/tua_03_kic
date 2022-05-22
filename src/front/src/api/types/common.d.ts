export type AccessLevelName = "ADMINISTRATOR" | "CLIENT" | "SPECIALIST";

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

export interface PaginationData {
  totalCount: number;
  totalPages: number;
  currentPage: number;
}

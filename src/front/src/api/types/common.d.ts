export interface Language {
  language: string;
}

export type AccessLevelName = "ADMINISTRATOR" | "CLIENT" | "SPECIALIST";

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

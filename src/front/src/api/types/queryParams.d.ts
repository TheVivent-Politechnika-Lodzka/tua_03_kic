export interface RemoveAccessLevelParams {
  login: string;
  accessLevel: string;
  tag: string;
}

export interface PaginationFilterParams {
  page: number;
  limit: number;
  phrase?: string;
}

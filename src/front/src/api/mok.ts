import axios from "axios";

export interface ListAccountsResponse {
  totalCount: number;
  totalPages: number;
  currentPage: number;
  data: AccountDetails[];
}

export interface ListAccountsRequest {
  page: number;
  limit: number;
  phrase?: string;
}

/**
 * zwraca listę kont, i informacje o paginacji
 *
 * @returns @example {totalCount, totalPages, currentPage, data} | {errorMessage, status}
 */
export async function listAccounts(params: ListAccountsRequest) {
  try {
    const { data } = await axios.get<ListAccountsResponse>("/mok/list", {
      params,
    });
    return data;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

export interface CreateAccountRequest {
  login: string;
  password: string;
  email: string;
  firstName: string;
  lastName: string;
  language: {
    language: string;
  };
}
export interface CreateAccountResponse extends AccountDetails, Etag {}

/**
 *
 * @param newAccount - nowe konto, bez poziomów dostępu
 * @returns @example CreateAccountResponse | {errorMessage, status}
 */
export async function createAccount(newAccount: CreateAccountRequest) {
  try {
    const { data, headers } = await axios.put<AccountDetails>(
      "/mok/create",
      newAccount
    );
    const etag = headers["etag"];
    return { ...data, etag } as CreateAccountResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

export interface GetAccountResponse extends AccountDetails, Etag {}

// pobierz dane konta
async function getAccountInternal(login: string) {
  try {
    const { data, headers } = await axios.get<AccountDetails>(`/mok/${login}`);
    const etag = headers["etag"];
    return { ...data, etag } as GetAccountResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

/**
 *
 * @returns @example GetAccountResponse | {errorMessage, status}
 */
export function getOwnAccount() {
  return getAccountInternal("");
}

/**
 *
 * @param login
 * @returns GetAccountResponse | {errorMessage, status}
 */
export function getAccount(login: string) {
  return getAccountInternal(login);
}

// zmień dane dowolnego konta
export interface EditAnyAccountRequest extends AccountDetails, Etag {}
export interface EditAnyAccountResponse extends AccountDetails, Etag {}

/**
 *
 * @param login          - login konta
 * @param accountDetails - nowe dane konta i etag
 * @returns @example EditAnyAccountResponse | {errorMessage, status}
 */
export async function editAnyAccount(
  login: string,
  accountDetails: EditAnyAccountRequest
) {
  try {
    const { etag, ...account } = accountDetails;
    const { data, headers } = await axios.put(`/mok/${login}`, account, {
      headers: {
        "If-Match": etag,
      },
    });
    const newEtag = headers["etag"];
    return { ...data, etag: newEtag } as EditAnyAccountResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

// zmień dane dowolnego konta
export interface EditOwnAccountRequest extends AccountDetails, Etag, Captcha {}
export interface EditOwnAccountResponse extends AccountDetails, Etag {}

/**
 *
 * @param accountDetails - nowe dane konta, etag i captcha
 * @returns @example EditOwnAccountResponse | {errorMessage, status}
 */
export async function editOwnAccount(accountDetails: EditOwnAccountRequest) {
  try {
    const { etag, ...account } = accountDetails;
    const { data, headers } = await axios.put("/mok", account, {
      headers: {
        "If-Match": etag,
      },
    });
    const newEtag = headers["etag"];
    return { ...data, etag: newEtag } as EditOwnAccountResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

export interface ChangeAnyPasswordRequest extends Taggable, Etag {
  newPassword: string;
}
export interface ChangeAnyPasswordResponse extends AccountDetails, Etag {}

/**
 *
 * @param login - login konta
 * @param passwordData - nowe hasło z id i wersją konta oraz etag
 * @returns @example ChangeAnyPasswordResponse | {errorMessage, status}
 */
export async function changeAnyPassword(
  login: string,
  passwordData: ChangeAnyPasswordRequest
) {
  try {
    const { etag, ...password } = passwordData;
    const { data, headers } = await axios.patch(
      `/mok/password/${login}`,
      password,
      {
        headers: {
          "If-Match": etag,
        },
      }
    );
    const newEtag = headers["etag"];
    return { ...data, etag: newEtag } as ChangeAnyPasswordResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

export interface ChangeOwnPasswordRequest extends Taggable, Etag, Captcha {
  oldPassword: string;
  newPassword: string;
}
export interface ChangeOwnPasswordResponse extends AccountDetails, Etag {}

/**
 *
 * @param passwordData - stare hasło, nowe hasło z id i wersją konta oraz etag i captcha
 * @returns @example ChangeOwnPasswordResponse | {errorMessage, status}
 */
export async function changeOwnPassword(
  passwordData: ChangeOwnPasswordRequest
) {
  try {
    const { etag, ...password } = passwordData;
    const { data, headers } = await axios.patch("/mok/password", password, {
      headers: {
        "If-Match": etag,
      },
    });
    const newEtag = headers["etag"];
    return { ...data, etag: newEtag } as ChangeOwnPasswordResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

export interface AddAccessLevelRequest extends AccessLevel, Etag {}
export interface AddAccessLevelResponse extends AccountDetails, Etag {}

/**
 *
 * @param login - login konta
 * @param AccessLevel - nowy poziom dostępu oraz etag
 * @returns @example AddAccessLevelResponse | {errorMessage, status}
 */
export async function addAccesLevel(
  login: string,
  AccessLevel: AddAccessLevelRequest
) {
  try {
    const { etag, ...accessLevel } = AccessLevel;
    const { data, headers } = await axios.put(
      `/mok/access-level/${login}`,
      accessLevel,
      {
        headers: {
          "If-Match": etag,
        },
      }
    );
    const newEtag = headers["etag"];
    return { ...data, etag: newEtag } as AddAccessLevelResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

export interface RemoveAccessLevelRequest extends Etag {
  level: AccessLevelType;
}
export interface RemoveAccessLevelResponse extends AccountDetails, Etag {}

/**
 *
 * @param login - login konta
 * @param accessLevelData - poziom dostępu oraz etag
 * @returns @example RemoveAccessLevelResponse | {errorMessage, status}
 */
export async function removeAccessLevel(
  login: string,
  accessLevelData: RemoveAccessLevelRequest
) {
  try {
    const { etag, level } = accessLevelData;
    const { data, headers } = await axios.delete(
      `/mok/access-level/${login}/${level}`,
      {
        headers: {
          "If-Match": etag,
        },
      }
    );
    const newEtag = headers["etag"];
    return { ...data, etag: newEtag } as RemoveAccessLevelResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

export interface DeactivateAccountResponse extends AccountDetails, Etag {}

/**
 * metoda może dłużej się ładować, ponieważ wysyła maila
 * @param login - login konta
 * @param etag  - etag konta
 * @returns @example DeactivateAccountResponse | {errorMessage, status}
 */
export async function deactivateAccount(login: string, etag: string) {
  try {
    const { data, headers } = await axios.patch(
      `/mok/deactivate/${login}`,
      {},
      {
        headers: {
          "If-Match": etag,
        },
      }
    );
    const newEtag = headers["etag"];
    return { ...data, etag: newEtag } as DeactivateAccountResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

export interface ActivateAccountResponse extends AccountDetails, Etag {}

/**
 * metoda może dłużej się ładować, ponieważ wysyła maila
 * @param login - login konta
 * @param etag  - etag konta
 * @returns @example ActivateAccountResponse | {errorMessage, status}
 */
export async function activateAccount(login: string, etag: string) {
  try {
    const { data, headers } = await axios.patch(
      `/mok/activate/${login}`,
      {},
      {
        headers: {
          "If-Match": etag,
        },
      }
    );
    const newEtag = headers["etag"];
    return { ...data, etag: newEtag } as ActivateAccountResponse;
  } catch (error) {
    if (axios.isAxiosError(error) && error.response) {
      return {
        errorMessage: error.response.data as string,
        status: error.response.status,
      } as ApiError;
    }
    throw error;
  }
}

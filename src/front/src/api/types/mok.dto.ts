export interface AccountEditDto {
  firstName: string;
  surname: string;
  email: string;
  phoneNumber: string;
}

export interface AccountDto {
  login: string;
  firstName: string;
  surname: string;
  email: string;
  phoneNumber: string;
}

export interface AccessLevelDto{
  level: string,
  email?: string,
  phoneNumber?: string;
  pesel?: string;
}

export interface AccountWithAccessLevelDto{
  login: string;
  firstName: string;
  surname: string;
  confirmed : boolean
  active : boolean
  accessLevels: AccessLevelDto[];
}
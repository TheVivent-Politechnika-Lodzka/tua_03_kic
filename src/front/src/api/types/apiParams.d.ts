import { AbstractDto, AccessLevelName, Language } from "./common";

interface ETag {
  ETag: string;
}

export interface RegisterClientDto {
  password: string;
  login: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  pesel: string;
  language: Language;
}

export interface RegisterClientConfirmDto {
  token: string;
  //   login: string;
}

export interface CreateAccountDto {
  password: string;
  login: string;
  email: string;
  firstName: string;
  lastName: string;
  language: Language;
}

export interface AccountActivationDto {
  login: string;
  tag: ETag;
}

export interface AccessLevelDto {
  level: AccessLevelName;
  phoneNumber?: string;
  contactEmail?: string;
  pesel?: string;
}

export interface AddAccessLevel {
  login: string;
  accessLevel: AccessLevelDto;
}

export interface RemoveAccessLevel {
  login: string;
  accessLevel: AccessLevelName;
  tag: ETag;
}

export interface changeOwnPasswordDto {
  oldPassword: string;
  newPassword: string;
  eTag: string;
}

export interface changePasswordDto {
  login: string;
  data: {
    newPassword: string;
    eTag: string;
  };
}

export interface AccountWithAccessLevelsDto extends AbstractDto {
  login: string;
  firstName: string;
  lastName: string;
  isActive: boolean;
  isConfirmed: boolean;
  language: Language;
  accessLevels: AccessLevelDto[];
}

export interface AccountWithAccessLevelsDtoWithLogin {
  login: string;
  data: AccountWithAccessLevelsDto;
}

export interface LoginCredentials {
  login: string;
  password: string;
}

export interface JustLogin {
  login: string;
}

export interface ResetPasswordTokenDto {
  login: string;
  token: string;
  password: string;
}

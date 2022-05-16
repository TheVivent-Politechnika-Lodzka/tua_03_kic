export interface Tag {
  tag: string;
}

export interface AccountDto extends Tag{
  login: string;
  firstName: string;
  lastName: string;
  active: boolean;
  confirmed: boolean;
}

export interface ChangePasswordDto {
  oldPassword: string;
  newPassword: string;
}

export interface AccessLevelDto{
  level: string,
  email?: string,
  phoneNumber?: string;
  pesel?: string;
}

export interface AccountWithAccessLevelDto extends Tag{
  login: string;
  firstName: string;
  lastName: string;
  confirmed : boolean
  active : boolean
  accessLevels: AccessLevelDto[];
}
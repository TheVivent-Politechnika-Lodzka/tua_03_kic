interface ApiError {
  errorMessage: string;
  status: number;
}

interface Taggable {
  id: string;
  version: number;
}

type AccessLevelType = "ADMINISTRATOR" | "CLIENT" | "SPECIALIST";

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
  isActive: boolean;
  isConfirmed: boolean;
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

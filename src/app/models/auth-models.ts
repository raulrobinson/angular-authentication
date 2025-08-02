export interface User {
  id?: number;
  email: string;
  name: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  confirmPassword: string;
}

export interface AuthResponse {
  messageId: string;
  token: string;
}

export interface ErrorResponse {
  code: number;
  message: string;
  identifier: string;
  timestamp: string;
  errors?: {
    message: string;
    parameter: string;
  }[];
}


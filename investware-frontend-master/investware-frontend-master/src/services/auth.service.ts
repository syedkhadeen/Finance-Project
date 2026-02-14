import axios from 'axios';

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:3002';
const API_PREFIX = '/v1/api';

export interface SignUpData {
  auth_id: string;
  email: string;
  firstName?: string;
  lastName?: string;
}

export interface LoginData {
  email: string;
  password: string;
}

export interface AuthResponse {
  data: {
    id_token: string;
  };
}

export interface UserProfile {
  id: string;
  auth_id: string;
  email: string;
  firstName?: string;
  lastName?: string;
  created_at: Date;
  updated_at: Date;
}

class AuthService {
  async signUp(data: SignUpData): Promise<any> {
    const response = await axios.post(`${API_BASE_URL}${API_PREFIX}/auth/signup`, data);
    return response.data;
  }

  async login(data: LoginData): Promise<AuthResponse> {
    const response = await axios.post(`${API_BASE_URL}${API_PREFIX}/auth/login`, data);
    if (response.data?.data?.id_token) {
      this.setToken(response.data.data.id_token);
    }
    return response.data;
  }

  setToken(token: string) {
    if (typeof window !== 'undefined') {
      localStorage.setItem('token', token);
    }
  }

  getToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('token');
    }
    return null;
  }

  removeToken() {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
    }
  }

  logout() {
    this.removeToken();
    if (typeof window !== 'undefined') {
      window.location.href = '/login';
    }
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  getAuthHeader() {
    const token = this.getToken();
    return token ? { Authorization: `Bearer ${token}` } : {};
  }
}

export default new AuthService();

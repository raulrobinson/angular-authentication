import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';
import { AuthResponse, LoginRequest, RegisterRequest, User } from '@models/auth-models';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private http = inject(HttpClient);

  private currentUserSubject = new BehaviorSubject<User | null>(null);
  private isAuthenticatedSubject = new BehaviorSubject<boolean>(false);

  constructor() {
    this.loadStoredUser();
  }

  private loadStoredUser(): void {
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');

    if (token && user) {
      try {
        const userObj: User = JSON.parse(user);
        this.currentUserSubject.next(userObj);
        this.isAuthenticatedSubject.next(true);
      } catch {
        this.clearStoredData();
      }
    }
  }

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('api/auth/login', credentials).pipe(
      tap(response => {
        const user: User = { email: credentials.email, name: credentials.email.split('@')[0] };
        this.setSession(response.token, user);
      })
    );
  }

  register(userData: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>('api/auth/register', userData).pipe(
      tap(response => {
        const user: User = { email: userData.email, name: userData.name };
        this.setSession(response.token, user);
      })
    );
  }

  logout(): void {
    const token = this.getToken();
    if (token) {
      this.http.get('/api/auth/logout', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }).subscribe({
        next: () => this.clearStoredData(),
        error: err => {
          console.warn('❌ Logout request failed, clearing local data anyway.', err);
          this.clearStoredData();
        }
      });
    } else {
      this.clearStoredData();
    }
  }

  private setSession(token: string, user: User): void {
    localStorage.setItem('token', token);
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSubject.next(user);
    this.isAuthenticatedSubject.next(true);
  }

  private clearStoredData(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.currentUserSubject.next(null);
    this.isAuthenticatedSubject.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): User | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    return this.isAuthenticatedSubject.value;
  }
}

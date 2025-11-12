import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { environment } from '../environments/enviroment';


export interface User {
  username: string;
  role: string;
}

interface LoginResponse {
  token: string;
  username: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private userSubject = new BehaviorSubject<User | null>(null);
  user$: Observable<User | null> = this.userSubject.asObservable();

  private baseUrl: string = environment.apiUrl;
  
  constructor(private http: HttpClient) {
    this.loadUserFromStorage();
  }

  private loadUserFromStorage() {
    const username = localStorage.getItem('username');
    const role = localStorage.getItem('role');
    if (username && role) {
      this.userSubject.next({ username, role });
    }
  }

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.baseUrl}/auth/login`, { username, password })
      .pipe(
        tap(response => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('username', response.username);
          localStorage.setItem('role', response.role);
          this.userSubject.next({ username: response.username, role: response.role });
        })
      );
  }

  register(name: string, username: string, email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/auth/register`, { name, username, email, password });
  }

  logout() {
    localStorage.clear();
    this.userSubject.next(null);
  }
}

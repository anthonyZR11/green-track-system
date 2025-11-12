import { CommonModule } from '@angular/common';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router, RouterModule, RouterOutlet } from '@angular/router';
import { AuthService, User } from './services/auth.service';
import { Subscription } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { ToastrModule } from 'ngx-toastr';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterModule,
    CommonModule,
    FormsModule
  ],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit, OnDestroy {
  username: string = '';
  role: string = '';

  user: User | null = null;
  private subscription?: Subscription;

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit() {
    this.subscription = this.authService.user$.subscribe(user => {
      this.user = user;
      this.username = user?.username || '';
      this.role = user?.role || '';
    });
  }

  ngOnDestroy() {
    this.subscription?.unsubscribe();
  }

  isAuthRoute() {
    return this.router.url === '/login' || this.router.url === '/registro';
  }

  isAdmin(): boolean {
    return this.role.toUpperCase() === 'ADMIN';
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  title = 'Green Track';
}

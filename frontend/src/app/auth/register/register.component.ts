import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent {
  name = '';
  username = '';
  email = '';
  password = '';
  error = '';
  success = false;

  constructor(private authService: AuthService, private router: Router) { }

  onRegister() {
    this.error = '';
    this.success = false;
    
    this.authService.register(this.name, this.username, this.email, this.password).subscribe({
      next: () => {
        this.success = true;
        setTimeout(() => this.router.navigate(['/login']), 1200);
      },
      error: err => {
        // Muestra el mensaje de error devuelto por el backend, si existe
        if (err.error && err.error.message) {
          this.error = err.error.message;
        } else {
          this.error = 'Error al registrar usuario';
        }
      }
    });
  }
}

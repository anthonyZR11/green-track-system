import { Component, OnInit } from '@angular/core';
import { User, UserService } from '../services/user.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Modal } from 'bootstrap';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-users',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './users.component.html',
  styleUrl: './users.component.css'
})
export class UsersComponent implements OnInit {
  users: User[] = [];
  selectedUser: User | null = null;
  isEditing = false;
  modal!: Modal;

  roles = ['USER', 'ADMIN'];

  constructor(private userService: UserService, private toastr: ToastrService) {}

  ngOnInit() {
    this.loadUsers();
    const modalElement = document.getElementById('userModal');
    if (modalElement) {
      this.modal = new Modal(modalElement);
    }
  }

  loadUsers() {
    this.userService.getUsers()
      .subscribe({
        next: (data) => this.users = data,
        error: (err) => {
          this.toastr.error('Error al cargar los usuarios', 'Error');
        }
      });
  }

  openModal(user?: User) {
    this.isEditing = !!user;
    if (user) {
      this.selectedUser = { ...user };
      // Limpiar password al editar (no se muestra)
      this.selectedUser.password = '';
    } else {
      this.selectedUser = { 
        id: 0, 
        name: '', 
        username: '', 
        email: '', 
        role: 'USER',
        password: ''
      } as User;
    }
    this.modal.show();
  }

  saveUser() {
    if (!this.selectedUser) return;

    // Preparar datos para enviar
    const userData: any = {
      id: this.selectedUser.id,
      name: this.selectedUser.name,
      username: this.selectedUser.username,
      email: this.selectedUser.email,
      role: this.selectedUser.role
    };

    // Solo incluir password si se proporcionó (y no está vacío)
    if (this.selectedUser.password && this.selectedUser.password.trim() !== '') {
      userData.password = this.selectedUser.password;
    }

    if (this.isEditing) {
      // Al editar, solo enviar password si se proporcionó uno nuevo
      this.userService.updateUser(this.selectedUser.id, userData)
        .subscribe({
          next: () => {
            this.loadUsers();
            this.modal.hide();
            this.toastr.success('Usuario actualizado correctamente', 'Éxito');
            this.clearSelection();
          },
          error: (err) => {
            if (err.status === 409) {
              this.toastr.warning('El usuario ya existe', 'Advertencia');
            } else {
              this.toastr.error('Error al guardar el usuario', 'Error');
            }
          }
        });
    } else {
      // Al crear, password es requerido
      if (!this.selectedUser.password || this.selectedUser.password.trim() === '') {
        this.toastr.warning('La contraseña es requerida', 'Advertencia');
        return;
      }
      this.userService.createUser(userData)
        .subscribe({
          next: () => {
            this.loadUsers();
            this.modal.hide();
            this.toastr.success('Usuario creado correctamente', 'Éxito');
            this.clearSelection();
          },
          error: (err) => {
            if (err.status === 409) {
              this.toastr.warning('El usuario ya existe', 'Advertencia');
            } else {
              this.toastr.error('Error al guardar el usuario', 'Error');
            }
          }
        });
    }
  }

  deleteUser(id: number) {
    if (confirm('¿Está seguro de que desea eliminar este usuario?')) {
      this.userService.deleteUser(id)
        .subscribe({
          next: () => {
            this.loadUsers();
            this.toastr.success('Usuario eliminado correctamente', 'Éxito');
          },
          error: (err) => {
            this.toastr.error('Error al eliminar el usuario', 'Error');
          }
        });
    }
  }

  clearSelection() {
    this.selectedUser = null;
    this.isEditing = false;
  }
}

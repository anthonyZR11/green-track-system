import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { UserService } from '../services/user.service';
import { EquipmentService, StatusEquiment } from '../services/equipment.service';
import { LoanService } from '../services/loan.service';
import { User } from '../services/user.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  username: string = '';
  equiposDisponibles: number = 0;
  prestamosActivos: number = 0;
  usuariosActivos: number = 0;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private equipmentService: EquipmentService,
    private loanService: LoanService
  ) {}

  ngOnInit() {
    this.authService.user$.subscribe(user => {
      this.username = user?.username || 'Usuario';
    });
    this.loadStats();
  }

  loadStats() {
    // Obtener total de usuarios
    this.userService.getUsers().subscribe({
      next: (users: User[]) => {
        this.usuariosActivos = users.length;
      },
      error: (err: any) => {
        console.error('Error al cargar usuarios:', err);
      }
    });

    // Obtener equipos disponibles
    this.equipmentService.getEquipments('', '', StatusEquiment.DISPONIBLE).subscribe({
      next: (equipments) => {
        this.equiposDisponibles = equipments.length;
      },
      error: (err) => {
        console.error('Error al cargar equipos disponibles:', err);
      }
    });

    // Obtener préstamos activos (puedes definir qué significa 'activo' según tu backend)
    // Por ejemplo, préstamos que no han sido devueltos aún
    this.loanService.getLoans(undefined, undefined, undefined).subscribe({
      next: (loans) => {
        console.log('Préstamos recibidos en Home:', loans);
        // Filtrar préstamos activos si el backend no lo hace
        this.prestamosActivos = loans.filter(loan => loan.status === 'ACTIVO').length;
      },
      error: (err) => {
        console.error('Error al cargar préstamos activos:', err);
      }
    });
  }
}

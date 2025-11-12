import { Component, OnInit } from '@angular/core';
import { Loan, LoanService } from '../services/loan.service';
import { User, UserService } from '../services/user.service';
import { Equipment, EquipmentService } from '../services/equipment.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Modal } from 'bootstrap';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-loans',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './loans.component.html',
  styleUrl: './loans.component.css'
})
export class LoansComponent implements OnInit {
  loans: Loan[] = [];
  users: User[] = [];
  equipments: Equipment[] = [];
  selectedLoan: Loan | null = null;
  isEditing = false;
  modal!: Modal;

  // Filtros
  filterUserId: number | null = null;
  filterStartDate: string = '';
  filterEndDate: string = '';
  
  // Campo para el formulario
  expectedReturnDate: string = '';

  constructor(
    private loanService: LoanService,
    private userService: UserService,
    private equipmentService: EquipmentService,
    private toastr: ToastrService
  ) {}

  ngOnInit() {
    this.loadUsers();
    this.loadEquipments();
    this.loadLoans();
    const modalElement = document.getElementById('loanModal');
    if (modalElement) {
      this.modal = new Modal(modalElement);
    }
  }

  loadUsers() {
    this.userService.getUsers()
      .subscribe({
        next: (data) => this.users = data,
        error: (err) => {
          console.error('Error al cargar usuarios:', err);
        }
      });
  }

  loadEquipments() {
    this.equipmentService.getEquipments('', '', '')
      .subscribe({
        next: (data) => this.equipments = data,
        error: (err) => {
          console.error('Error al cargar equipos:', err);
        }
      });
  }

  loadLoans() {
    const userId = this.filterUserId !== null ? this.filterUserId : undefined;
    // Convertir fechas a formato ISO completo
    let startDate: string | undefined = undefined;
    let endDate: string | undefined = undefined;
    
    if (this.filterStartDate && this.filterStartDate.trim() !== '') {
      startDate = `${this.filterStartDate}T00:00:00`;
    }
    
    if (this.filterEndDate && this.filterEndDate.trim() !== '') {
      endDate = `${this.filterEndDate}T23:59:59`;
    }

    // Limpiar la lista antes de cargar para evitar mostrar datos antiguos
    this.loans = [];

    this.loanService.getLoans(userId, startDate, endDate)
      .subscribe({
        next: (data) => {
          this.loans = data || [];
        },
        error: (err) => {
          this.toastr.error('Error al cargar los préstamos', 'Error');
          this.loans = [];
        }
      });
  }

  applyFilters() {
    this.loadLoans();
  }

  clearFilters() {
    this.filterUserId = null;
    this.filterStartDate = '';
    this.filterEndDate = '';
    this.loadLoans();
  }

  openModal(loan?: Loan) {
    this.isEditing = !!loan;
    if (loan) {
      this.selectedLoan = { ...loan };
      // Extraer solo la fecha (YYYY-MM-DD) si viene en formato ISO
      if (loan.expectedReturnDate) {
        this.expectedReturnDate = loan.expectedReturnDate.split('T')[0];
      } else {
        this.expectedReturnDate = '';
      }
    } else {
      this.selectedLoan = { 
        id: 0, 
        userId: 0, 
        equipmentId: 0, 
        loanDate: '', 
        expectedReturnDate: '', 
        status: 'ACTIVO' 
      } as Loan;
      this.expectedReturnDate = '';
    }
    this.modal.show();
  }

  private getErrorMessage(err: any): string {
    // Primero intentar obtener el mensaje del array errors
    if (err.error?.errors && Array.isArray(err.error.errors) && err.error.errors.length > 0) {
      return err.error.errors[0];
    }
    // Si no, intentar obtener el mensaje directo
    if (err.error?.message) {
      return err.error.message;
    }
    // Si no, intentar obtener el error como string
    if (typeof err.error === 'string') {
      return err.error;
    }
    // Mensaje por defecto
    return 'Error al guardar el préstamo';
  }

  saveLoan() {
    if (!this.selectedLoan) return;

    // Preparar datos para enviar
    const loanData: any = {
      id: this.selectedLoan.id,
      userId: this.selectedLoan.userId,
      equipmentId: this.selectedLoan.equipmentId,
      status: this.selectedLoan.status
    };

    // Solo incluir expectedReturnDate si se proporcionó, convertir a formato ISO completo
    if (this.expectedReturnDate && this.expectedReturnDate.trim() !== '') {
      // Convertir fecha YYYY-MM-DD a formato ISO completo con hora 23:59:59
      loanData.expectedReturnDate = `${this.expectedReturnDate}T23:59:59`;
    }

    if (this.isEditing) {
      this.loanService.updateLoan(this.selectedLoan.id, loanData)
        .subscribe({
          next: () => {
            this.loadLoans();
            this.modal.hide();
            this.toastr.success('Préstamo actualizado correctamente', 'Éxito');
            this.clearSelection();
          },
          error: (err) => {
            const errorMessage = this.getErrorMessage(err);
            if (err.status === 409) {
              this.toastr.warning(errorMessage, 'Advertencia');
            } else {
              this.toastr.error(errorMessage, 'Error');
            }
          }
        });
    } else {
      this.loanService.createLoan(loanData)
        .subscribe({
          next: () => {
            this.loadLoans();
            this.modal.hide();
            this.toastr.success('Préstamo creado correctamente', 'Éxito');
            this.clearSelection();
          },
          error: (err) => {
            const errorMessage = this.getErrorMessage(err);
            if (err.status === 409) {
              this.toastr.warning(errorMessage, 'Advertencia');
            } else {
              this.toastr.error(errorMessage, 'Error');
            }
          }
        });
    }
  }

  returnLoan(id: number) {
    if (confirm('¿Está seguro de que desea devolver este equipo?')) {
      this.loanService.returnLoan(id)
        .subscribe({
          next: () => {
            this.loadLoans();
            this.loadEquipments(); // Recargar equipos para actualizar estados
            this.toastr.success('Equipo devuelto correctamente', 'Éxito');
          },
          error: (err) => {
            if (err.status === 409) {
              const errorMessage = err.error?.message || err.error || 'El préstamo ya ha sido devuelto o no se puede devolver en este estado';
              this.toastr.warning(errorMessage, 'Advertencia');
            } else {
              this.toastr.error('Error al devolver el equipo', 'Error');
            }
          }
        });
    }
  }

  clearSelection() {
    this.selectedLoan = null;
    this.isEditing = false;
    this.expectedReturnDate = '';
  }

  getUserName(userId: number): string {
    const user = this.users.find(u => u.id === userId);
    return user ? user.name : `Usuario ${userId}`;
  }

  getEquipmentName(equipmentId: number): string {
    const equipment = this.equipments.find(e => e.id === equipmentId);
    return equipment ? `${equipment.name} (${equipment.type})` : `Equipo ${equipmentId}`;
  }
}

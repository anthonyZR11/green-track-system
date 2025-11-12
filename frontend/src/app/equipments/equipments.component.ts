import { Component, OnInit } from '@angular/core';
import { Equipment, EquipmentService, StatusEquiment } from '../services/equipment.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { Modal } from 'bootstrap';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-equipments',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './equipments.component.html',
  styleUrl: './equipments.component.css'
})
export class EquipmentsComponent implements OnInit {
  equipments: Equipment[] = [];
  selectedEquipment: Equipment | null = null;
  isEditing = false;

  filterType = '';
  filterBrand = '';
  filterStatus: StatusEquiment | '' = '';
  statusList = Object.values(StatusEquiment);

  modal!: Modal;

  clearSelection() {
    this.selectedEquipment = null;
    this.isEditing = false;
  }

  applyFilters() {
    this.loadEquipments();
  }

  clearFilters() {
    this.filterType = '';
    this.filterBrand = '';
    this.filterStatus = '';
    this.loadEquipments();
  }

  constructor(private equipmentService: EquipmentService, private toastr: ToastrService) { }

  ngOnInit() {
    this.loadEquipments();
    const modalElement = document.getElementById('equipmentModal');
    // Instancia modal Bootstrap
    this.modal = new Modal(modalElement!);
  }

  loadEquipments() {
    this.equipmentService.getEquipments(this.filterType, this.filterBrand, this.filterStatus)
      .subscribe(data => this.equipments = data);
  }

  selectEquipment(equipment: Equipment) {
    this.selectedEquipment = { ...equipment };
    this.isEditing = true;
  }

  openModal(equipment?: Equipment) {
    this.isEditing = !!equipment;
    if (equipment) {
      this.selectedEquipment = { ...equipment };
    } else {
      this.selectedEquipment = { id: 0, name: '', type: '', brand: '', status: this.statusList[0] } as Equipment;
    }
    this.modal.show();
  }

  saveEquipment() {
    if (!this.selectedEquipment) return;

    if (this.isEditing) {
      this.equipmentService.updateEquipment(this.selectedEquipment.id, this.selectedEquipment)
        .subscribe({
          next: () => {
            this.loadEquipments();
            this.modal.hide();
            this.toastr.success('Equipo actualizado correctamente', 'Éxito');
            this.clearSelection();
          },
          error: (err) => {
            if (err.status === 409) {
              this.toastr.warning('El equipo ya existe', 'Advertencia');
            } else {
              this.toastr.error('Error al guardar el equipo', 'Error');
            }
          }
        });
    } else {
      this.equipmentService.createEquipment(this.selectedEquipment)
        .subscribe({
          next: () => {
            this.loadEquipments();
            this.modal.hide();
            this.toastr.success('Equipo creado correctamente', 'Éxito');
            this.clearSelection();
          },
          error: (err) => {
            if (err.status === 409) {
              this.toastr.warning('El equipo ya existe', 'Advertencia');
            } else {
              this.toastr.error('Error al guardar el equipo', 'Error');
            }
          }
        });
    };
  }

  deleteEquipment(id: number) {
    this.equipmentService.deleteEquipment(id)
      .subscribe(() => this.loadEquipments());
  }
}
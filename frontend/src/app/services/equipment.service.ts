import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/enviroment';

export interface Equipment {
  id: number;
  name: string;
  type: string;
  brand: string;
  status: StatusEquiment;
}

export enum StatusEquiment {
  DISPONIBLE = 'DISPONIBLE',
  PRESTADO = 'PRESTADO',
  MANTENIMIENTO = 'MANTENIMIENTO',
  // agrega los estados que necesites
}

@Injectable({
  providedIn: 'root'
})
export class EquipmentService {
  private baseUrl: string = environment.apiUrl + '/v1/equipments';

  constructor(private http: HttpClient) {}

  getEquipments(filterType: string, filterBrand: string, filterStatus: StatusEquiment | ''): Observable<Equipment[]> {
  const params: any = {};
  if (filterType) params.type = filterType;
  if (filterBrand) params.brand = filterBrand;
  if (filterStatus) params.status = filterStatus;

  return this.http.get<Equipment[]>(this.baseUrl + '/filter', { params });
}

  getEquipmentById(id: number): Observable<Equipment> {
    return this.http.get<Equipment>(`${this.baseUrl}/${id}`);
  }

  createEquipment(equipment: Equipment): Observable<Equipment> {
    return this.http.post<Equipment>(this.baseUrl, equipment);
  }

  updateEquipment(id: number, equipment: Equipment): Observable<Equipment> {
    return this.http.put<Equipment>(`${this.baseUrl}/${id}`, equipment);
  }

  deleteEquipment(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}

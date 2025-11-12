import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../environments/enviroment';

export interface Loan {
  id: number;
  userId: number;
  userName?: string;
  equipmentId: number;
  equipmentName?: string;
  loanDate?: string;
  expectedReturnDate?: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class LoanService {
  private baseUrl: string = environment.apiUrl + '/v1/loans';

  constructor(private http: HttpClient) {}

  getLoans(userId?: number, startDate?: string, endDate?: string): Observable<Loan[]> {
    const params: any = {};
    if (userId) params.userId = userId;
    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;

    return this.http.get<Loan[]>(this.baseUrl + '/search', { params });
  }

  getLoanById(id: number): Observable<Loan> {
    return this.http.get<Loan>(`${this.baseUrl}/${id}`);
  }

  createLoan(loan: Loan): Observable<Loan> {
    return this.http.post<Loan>(this.baseUrl, loan);
  }

  updateLoan(id: number, loan: Loan): Observable<Loan> {
    return this.http.put<Loan>(`${this.baseUrl}/${id}`, loan);
  }

  returnLoan(id: number): Observable<Loan> {
    return this.http.put<Loan>(`${this.baseUrl}/${id}/return`, {});
  }
}



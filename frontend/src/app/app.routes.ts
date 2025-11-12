import { Routes } from '@angular/router';
import { EquipmentsComponent } from './equipments/equipments.component';
import { LoansComponent } from './loans/loans.component';
import { UsersComponent } from './users/users.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { AuthGuard } from './core/auth.guard';
import { ChartsComponent } from './charts/charts.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegisterComponent },
  {
    path: '',
    canActivate: [AuthGuard],
    children: [
      { path: 'home', component: HomeComponent },
      { path: 'equipos', component: EquipmentsComponent },
      { path: 'prestamos', component: LoansComponent },
      { path: 'usuarios', component: UsersComponent },
      { path: 'graficos', component: ChartsComponent },
      { path: '', redirectTo: 'home', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: '/home' }
];

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterComponent } from './accounts/register/register.component';
import { LoginComponent } from './accounts/login/login.component';
import { PatientSummaryPageComponent } from './patient/patient-summary-page/patient-summary-page.component';
import {PatientStatisticsAreaComponent} from './patient/patient-statistics-area/patient-statistics-area.component';
import {PatientDetailsAreaComponent} from './patient/patient-details-area/patient-details-area.component';
import { DashboardComponent } from './accounts/dashboard/dashboard.component';
import { AuthGuard } from './accounts/auth.guard';
import { HomeComponent } from './home/home.component';
import { EditProfileComponent } from './accounts/edit-profile/edit-profile.component';


const routes: Routes = [
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'profile',
    component: EditProfileComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'patient/:id',
    component: PatientSummaryPageComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'statistics',
        component: PatientStatisticsAreaComponent
      },
      {
        path: 'details',
        component: PatientDetailsAreaComponent
      }
    ]
  },
  {
    path: '**',
    component: HomeComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { CookieService } from 'ngx-cookie-service';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { DashboardComponent } from './dashboard/dashboard.component';
import { PatientModule } from '../patient/patient.module'
import { MatCardModule } from '@angular/material/card';
import { EditProfileComponent } from './edit-profile/edit-profile.component';

@NgModule({
  declarations: [LoginComponent, RegisterComponent, DashboardComponent, EditProfileComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    MatInputModule,
    MatButtonModule,
    MatSnackBarModule,
    PatientModule,
    MatCardModule
  ],
  providers: [
    CookieService
  ],
  exports: [
    LoginComponent,
    RegisterComponent,
    EditProfileComponent
  ]
})
export class AccountsModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddPatientComponent } from './add-patient/add-patient.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSnackBarModule, MAT_SNACK_BAR_DEFAULT_OPTIONS } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card'
import { PatientListComponent } from './patient-list/patient-list.component';
import { PatientSummaryPageComponent} from './patient-summary-page/patient-summary-page.component';
import { RouterModule } from '@angular/router';
import { MatTabsModule } from '@angular/material/tabs';
import { PatientStatisticsAreaComponent } from './patient-statistics-area/patient-statistics-area.component';
import { PatientDetailsAreaComponent } from './patient-details-area/patient-details-area.component';
import { WorryModule } from '../worry/worry.module';
import { MoodModule } from '../mood/mood.module';
import { GraphModule } from '../charts/graph.module';
import { MatDialogModule } from '@angular/material/dialog'

@NgModule({
  declarations: [AddPatientComponent, PatientListComponent, PatientSummaryPageComponent, PatientStatisticsAreaComponent, PatientDetailsAreaComponent],
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        MatInputModule,
        MatButtonModule,
        MatSnackBarModule,
        MatCardModule,
        RouterModule,
        MatTabsModule,
        WorryModule,
        MoodModule,
        GraphModule,
        MatDialogModule
    ],
  exports: [
    AddPatientComponent,
    PatientListComponent
  ],
  providers: [
    {provide: MAT_SNACK_BAR_DEFAULT_OPTIONS, useValue: {duration: 2500}}
  ]
})
export class PatientModule { }

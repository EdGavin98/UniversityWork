import { Component, OnInit } from '@angular/core';
import { PatientService } from '../patient.service';
import { ActivatedRoute } from '@angular/router';
import { Patient } from '../patient';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-patient-details',
  templateUrl: './patient-summary-page.component.html',
  styleUrls: ['./patient-summary-page.component.css']
})
export class PatientSummaryPageComponent implements OnInit {

  constructor(private patientService: PatientService,
              private activatedRoute: ActivatedRoute,
              private snackBar: MatSnackBar) { }

  currentPatient: Patient;
  loading = true;
  statsPath = 'statistics';
  detailsPath = 'details';

  ngOnInit(): void {
    const paramMap = this.activatedRoute.snapshot.paramMap;
    const id = paramMap.get('id');

    this.patientService.loadInitialPatientData(id);
    this.patientService.patientGetStatus.subscribe(
      (status: number) => {
        console.log(status)
        if (status === 403) {
          this.snackBar.open('Error: You do not have permission to view this patient');
        } else if (status === 404) {
          this.snackBar.open('Error: This patient does not exist');
        } else if (status != 200 && status != 999) {
          this.snackBar.open('Network error: Please try again later');
        }
      }
    )

    this.patientService.patientData.subscribe(
      (patient: Patient) => {
        console.log(patient)
        if (patient != null) {
          this.currentPatient = patient
          this.loading = false
        } else [
          this.loading = true
        ]
      }
    )
  }
}

import { Component, OnInit } from '@angular/core';
import { PatientService } from '../patient.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-patient',
  templateUrl: './add-patient.component.html',
  styleUrls: ['./add-patient.component.css']
})
export class AddPatientComponent implements OnInit {

  constructor(
    private patientService : PatientService,
    private snackBar : MatSnackBar ) { }

  patientEmail : String;
  addCode : String;

  ngOnInit() : void {
  }

  onSubmit() : void {
    this.patientService.addAccountLink(this.patientEmail)
    .subscribe(
      (response : HttpResponse<any>)  => {
        this.snackBar.open("Patient Added Successfully");
        this.patientService.updateLinkData()
        this.patientEmail = ""
      },
      (error : HttpErrorResponse) => {
        if (error.status == 404) {
          this.snackBar.open("Error: No patient exists with this email")
        } else {
          this.snackBar.open("Network error: Please try again later")
        }
      }
    )
  }

}

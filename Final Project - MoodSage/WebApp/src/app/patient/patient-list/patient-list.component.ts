import { Component, OnInit } from '@angular/core';
import { PatientLink } from '../patientlink';
import { PatientService } from '../patient.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogData } from 'src/app/utils/confirm-dialog/confirm-dialog-data';
import { ConfirmDialogComponent } from 'src/app/utils/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-patient-list',
  templateUrl: './patient-list.component.html',
  styleUrls: ['./patient-list.component.css']
})
export class PatientListComponent implements OnInit {

  constructor(
    private patientService : PatientService,
    private snackBar : MatSnackBar,
    private router : Router,
    private dialog: MatDialog) {}

  activePatientList : PatientLink[] = [];
  pendingPatientList : PatientLink[] = [];

  ngOnInit(): void {
    this.getAllPatients()    
  }

  viewPatient(id : String) {
    this.router.navigate(["patient/", id])
  }

  onRemovePatient(id : String) {
    const confirm: ConfirmDialogData = {
      title: "Are you sure?",
      message: "Are you sure you want to remove this patient?"
    }


    const dialogRef = this.dialog.open(ConfirmDialogComponent, {height: "600", width: "200", data: confirm})
    dialogRef.afterClosed().subscribe((isConfirmed: Boolean) => {
      if (isConfirmed) {
        this.deletePatient(id)
      } else {
        this.snackBar.open("Cancelled patient removal")
      }
    })
  }

  private getAllPatients() {
    this.patientService.patientLinks.subscribe(
      (links: PatientLink[]) => {
        if (links != null) {
          this.activePatientList = links.filter(item => item.status == 1);
          this.pendingPatientList = links.filter(item => item.status == 0);
        }
      }
    )
  }

  private deletePatient(id: String) {
    this.patientService.removePatient(id).subscribe(
      (response : HttpResponse<any>) => {
        this.snackBar.open("Patient removed successfully")
        this.patientService.updateLinkData()
      },
      (error : HttpErrorResponse) => {
        if (error.status == 404) {
          this.snackBar.open("Error: No link exists for this patient. They may have already been removed.")
          this.patientService.updateLinkData()
        } else {
          this.snackBar.open("Network error: Please try again later")
        }
      }
    )
  }

}

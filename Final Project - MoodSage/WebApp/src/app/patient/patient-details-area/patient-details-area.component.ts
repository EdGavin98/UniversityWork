import {Component, Input, OnInit} from '@angular/core';
import {Patient} from '../patient';
import { PatientService } from '../patient.service';

@Component({
  selector: 'app-patient-details-area',
  templateUrl: './patient-details-area.component.html',
  styleUrls: ['./patient-details-area.component.css']
})
export class PatientDetailsAreaComponent implements OnInit {

  constructor(private patientService : PatientService) { }

  patient: Patient;

  ngOnInit(): void {
    this.patientService.patientData.subscribe(
      (data: Patient) => {
        this.patient = data;
      }
    )
  }

}

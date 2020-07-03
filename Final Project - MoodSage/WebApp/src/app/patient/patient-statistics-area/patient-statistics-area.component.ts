import {Component, Input, OnInit} from '@angular/core';
import {Patient} from '../patient';
import { PatientService } from '../patient.service';

@Component({
  selector: 'app-patient-statistics-area',
  templateUrl: './patient-statistics-area.component.html',
  styleUrls: ['./patient-statistics-area.component.css']
})
export class PatientStatisticsAreaComponent implements OnInit {

  constructor(private patientService: PatientService) { }

  patient: Patient

  ngOnInit(): void {
    this.patientService.patientData.subscribe(
      (data: Patient) => {
        this.patient = data;
      }
    )
  }

}

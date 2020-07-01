import { Component, OnInit } from '@angular/core';
import { AccountService } from '../account.service';
import { Patient } from 'src/app/patient/patient';
import { PatientService } from 'src/app/patient/patient.service';
import { Profile } from '../profile';
import { PatientLink } from 'src/app/patient/patientlink';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {



  constructor(private accountService: AccountService, private patientService : PatientService) { }

  profile: Profile = null;
  linkCount: number;
  loading = true;

  ngOnInit(): void {
    this.accountService.getProfile()
    
    this.accountService.currentUser.subscribe((user: Profile) => {
      if (user != null) {
        this.profile = user
        this.loading = false
      }
    })

    this.patientService.patientLinks.subscribe((links: PatientLink[]) => {
      if (links != null) {
        this.linkCount = links.length;
      } else {
        this.linkCount = 0;
      }
    })
  }

}

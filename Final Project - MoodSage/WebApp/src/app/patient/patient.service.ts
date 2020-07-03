import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { map } from 'rxjs/operators'
import { PatientLink } from './patientlink';
import { BehaviorSubject, Observable } from 'rxjs';
import { Patient } from './patient';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  /**
   * 
   */
  private _patientGetStatus: BehaviorSubject<number>
  public patientGetStatus: Observable<number>

 /**
  * Observable for patient data, only service can modify behaviour subject
  * Observable (patientData) exposed so that components can react to the changes
  */
 private _patientData: BehaviorSubject<Patient>
 public patientData: Observable<Patient>
 /**
  * Observable for patient link data, only service can modify behaviour subject
  * Observable (patientLinks) exposed so that components can react to the changes
  */
 private _patientLinks: BehaviorSubject<PatientLink[]>
 public patientLinks: Observable<PatientLink[]>

  constructor(private http : HttpClient, private cookieService : CookieService) { 
    this._patientGetStatus = new BehaviorSubject(999)
    this._patientLinks = new BehaviorSubject(null)
    this._patientData= new BehaviorSubject(null)

    this.patientGetStatus = this._patientGetStatus.asObservable()
    this.patientLinks = this._patientLinks.asObservable()
    this.patientData = this._patientData.asObservable()

    this.updateLinkData()
  }

  

  /**
   * Predefined default options for all network calls
   */
  private options = {
    headers : new HttpHeaders({"Authorization" : `Bearer ${this.cookieService.get('userToken')}`})
  };

  private uri = "https://api.stsoft.tech/api"

  
  addAccountLink(email: String) {
    var accountEmail = {
      email : email 
    }
    console.log(`${this.uri}/link`);
    return this.http.post(`${this.uri}/profile/links`, accountEmail, this.options);
  }

  getPatientData(id: String) {
    return this.http.get(`${this.uri}/patient/${id}`, this.options)
  }

  removePatient(id: String) {
    return this.http.delete(`${this.uri}/profile/link/${id}`, this.options)
  }

  getLinkedPatients() {
    return this.http.get<PatientLink[]>(`${this.uri}/profile/links`, this.options);
  }


  updateLinkData() {
    this.http.get<PatientLink[]>(`${this.uri}/profile/links`, this.options).subscribe(
      (response: PatientLink[]) => {
        this._patientLinks.next(response)
      },
      (error: HttpErrorResponse) => {
        console.log(error.status)
        console.log(this.options)
        this._patientLinks.next(null)
      }
    )
  }

  loadInitialPatientData(id: String) {
    this.http.get(`${this.uri}/patient/${id}`, this.options).subscribe(
      (response: Patient) => {
        this._patientData.next(new Patient(response))
        this._patientGetStatus.next(200)
      },
      (error: HttpErrorResponse) => {
        this._patientData.next(null)
        this._patientGetStatus.next(error.status)
        return error.status
      }
    )
  }
}

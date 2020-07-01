import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddPatientComponent } from './add-patient.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { PatientService } from '../patient.service';
import { of, throwError } from 'rxjs';
import { By } from '@angular/platform-browser';
import { Patient } from '../patient';

describe('AddPatientComponent', () => {
  let component: AddPatientComponent;
  let fixture: ComponentFixture<AddPatientComponent>;
  let patientService: PatientService;
  let snackBar: MatSnackBar;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddPatientComponent ],
      imports : [HttpClientTestingModule, MatSnackBarModule],
      providers : [CookieService, MatSnackBar,
      {
        provide: PatientService,
        useValue: {
          addAccountLink: (email: String) => of({status : 200}),
          updateLinkData: () => of({})
        }
      },
      {
        provide: MatSnackBar,
        useValue : jasmine.createSpyObj(MatSnackBar, ["open"])
      }
    ]
    })
    .compileComponents();

    patientService = TestBed.get(PatientService)
    snackBar = TestBed.get(MatSnackBar)

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddPatientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should clear field and display snackbar on success', () => {
      component.patientEmail = "e@e.com";
      component.onSubmit();

      fixture.detectChanges();

      expect(component.patientEmail).toBe("");
      expect(snackBar.open).toHaveBeenCalled();
  });
  it('should keep field data and display snackbar on error', () => {
    spyOn(patientService, "addAccountLink")
    .and
    .returnValue(throwError(""))
    
    component.patientEmail = "e@e.com";
    component.onSubmit();

    fixture.detectChanges();

    

    expect(component.patientEmail).toBe("e@e.com");
    expect(snackBar.open).toHaveBeenCalled();
  })
});

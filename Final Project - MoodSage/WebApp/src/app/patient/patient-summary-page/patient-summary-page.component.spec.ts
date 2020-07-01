import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { PatientSummaryPageComponent } from './patient-summary-page.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';

describe('PatientSummaryPageComponent', () => {
  let component: PatientSummaryPageComponent;
  let fixture: ComponentFixture<PatientSummaryPageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatientSummaryPageComponent ],
      imports : [HttpClientTestingModule, RouterTestingModule, MatSnackBarModule],
      providers : [CookieService]

    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientSummaryPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

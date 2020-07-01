import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientDetailsAreaComponent } from './patient-details-area.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';

describe('PatientDetailsAreaComponent', () => {
  let component: PatientDetailsAreaComponent;
  let fixture: ComponentFixture<PatientDetailsAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatientDetailsAreaComponent ],
      imports : [HttpClientTestingModule],
      providers : [CookieService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientDetailsAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
});

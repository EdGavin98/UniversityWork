import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientStatisticsAreaComponent } from './patient-statistics-area.component';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';

describe('PatientStatisticsAreaComponent', () => {
  let component: PatientStatisticsAreaComponent;
  let fixture: ComponentFixture<PatientStatisticsAreaComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PatientStatisticsAreaComponent ],
      imports : [HttpClientTestingModule],
      providers : [CookieService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PatientStatisticsAreaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardComponent } from './dashboard.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';
import { MatCardModule } from '@angular/material/card';
import { AccountService } from '../account.service';
import { By } from '@angular/platform-browser';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;
  let accountService: AccountService;

  beforeEach(async(() => {

    TestBed.configureTestingModule({
      declarations: [ DashboardComponent ],
      imports: [HttpClientTestingModule, MatCardModule],
      providers : [CookieService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should display information on correct loading', () => {
    component.profile = {
      forename : "bob",
      surname : "bobson",
      email : "bob@bobson.com"
    };
    component.linkCount = 1;
    component.loading = false;

    fixture.detectChanges();


    expect(fixture.debugElement.query(By.css("#title")).nativeElement.textContent.trim()).toEqual("Hello bob bobson")
    expect(fixture.debugElement.query(By.css("#email")).nativeElement.textContent.trim()).toEqual("Email: bob@bobson.com")
    expect(fixture.debugElement.query(By.css("#total")).nativeElement.textContent.trim()).toEqual("Total Patients: 1")
    expect(fixture.debugElement.query(By.css('p'))).toBeFalsy();

  });
  it('stay on loading on error (shouldn\'t occur as this would imply a lack of token, which the authguard should stop)', () => {
    component.loading = true;

    fixture.detectChanges()
    expect(fixture.debugElement.query(By.css('p'))).toBeTruthy();
    expect(fixture.debugElement.query(By.css('.dashContainer'))).toBeFalsy();


  });
  
});

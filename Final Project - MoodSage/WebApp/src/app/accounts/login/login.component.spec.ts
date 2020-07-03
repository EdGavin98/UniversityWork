import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginComponent } from './login.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AccountService } from '../account.service';
import { Observable, of } from 'rxjs';
import { HttpResponse } from '@angular/common/http';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockSnack : MatSnackBar

  beforeEach(async(() => {
    mockSnack = jasmine.createSpyObj(['open'])
    TestBed.configureTestingModule({
      declarations: [ LoginComponent ],
      imports : [HttpClientTestingModule, RouterTestingModule, MatSnackBarModule, FormsModule, ReactiveFormsModule],
      providers : [CookieService,
      {
        provide: AccountService,
        useValue: {
          login: () => of({status : 404})
        }
      },
      {
        provide: MatSnackBar,
        useValue: mockSnack
      }]

    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should have disabled button when no fields are filled', () => {
    expect(fixture.nativeElement.querySelector('button').disabled).toBeTruthy()
  });
  it('should have disabled button when one fields is filled', () => {
    component.email.setValue("email@email.com");
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('button').disabled).toBeTruthy()

  });
  it('should have enabled button when both fields are filled', () => {
    component.email.setValue("email@email.com");
    component.password.setValue("wdawdasdawd");
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('button').disabled).toBeFalsy()
  });
  it('should open snackbar on error', () => {
    component.onSubmit();

    fixture.detectChanges();
    expect(mockSnack.open).toHaveBeenCalled()
  })
});

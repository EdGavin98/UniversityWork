import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterComponent } from './register.component';
import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AccountService } from '../account.service';
import { throwError, of } from 'rxjs';
import { MatDialogModule } from '@angular/material/dialog';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let accountService : AccountService;
  let mockSnack: MatSnackBar;

  beforeEach(async(() => {
    mockSnack = jasmine.createSpyObj(MatSnackBar, ["open"]);
    TestBed.configureTestingModule({
      declarations: [ RegisterComponent],
      imports : [HttpClientTestingModule, RouterTestingModule, MatSnackBarModule, FormsModule, ReactiveFormsModule],
      providers : [CookieService,
      {
        provide: AccountService,
        useValue: {
          registerAccount: () => of({status : 200})
        }, 
      },
      {
        provide: MatSnackBar,
        useValue: mockSnack
      }]
    })
    .compileComponents();

    accountService = TestBed.get(AccountService)

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should disable submit if all fields are empty', () => {
    expect(fixture.nativeElement.querySelector('button').disabled).toBeTruthy()

  });
  it('should disable submit if some fields are empty', () => {
    component.registerForm.get("forename").setValue("feafeaf")
    component.registerForm.get("surname").setValue("feafeaf")
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('button').disabled).toBeTruthy()

  });
  it('should disable submit if password dont match', () => {
    component.registerForm.get("forename").setValue("feafeaf")
    component.registerForm.get("surname").setValue("feafeaf")
    component.registerForm.get("email").setValue("dwdawd@gmail.com")
    component.registerForm.get("password").setValue("password")
    component.registerForm.get("passwordConfirm").setValue("no")
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('button').disabled).toBeTruthy()
    expect(component.registerForm.valid).toBeFalsy()

  });
  it('should disable submit if email is invalid', () => {
    component.registerForm.get("forename").setValue("feafeaf")
    component.registerForm.get("surname").setValue("feafeaf")
    component.registerForm.get("email").setValue("dwdawdwdawda")
    component.registerForm.get("password").setValue("password")
    component.registerForm.get("passwordConfirm").setValue("password")
    fixture.detectChanges();

    expect(fixture.nativeElement.querySelector('button').disabled).toBeTruthy()
    
    expect(component.registerForm.valid).toBeFalsy()
  });
  it('should show snackbar if there was an error', () => {
    component.registerForm.get("forename").setValue("feafeaf")
    component.registerForm.get("surname").setValue("feafeaf")
    component.registerForm.get("email").setValue("dwdawdwdawda")
    component.registerForm.get("password").setValue("password")
    component.registerForm.get("passwordConfirm").setValue("password")

    spyOn(accountService, 'registerAccount')
    .and
    .returnValue(throwError(''))

    component.onSubmit();

    expect(mockSnack.open).toHaveBeenCalled();
  });
});

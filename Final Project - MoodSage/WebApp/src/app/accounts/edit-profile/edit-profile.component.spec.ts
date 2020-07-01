import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditProfileComponent } from './edit-profile.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CookieService } from 'ngx-cookie-service';
import { AccountService } from '../account.service';
import { of } from 'rxjs';
import { MatDialogModule } from '@angular/material/dialog';
import { By } from '@angular/platform-browser';

describe('EditProfileComponent', () => {
  let component: EditProfileComponent;
  let fixture: ComponentFixture<EditProfileComponent>;
  let mockSnack: MatSnackBar;

  beforeEach(async(() => {
    mockSnack = jasmine.createSpyObj(MatSnackBar, ["open"]);
    TestBed.configureTestingModule({
      declarations: [ EditProfileComponent ],
      imports : [HttpClientTestingModule, RouterTestingModule, MatSnackBarModule, FormsModule, ReactiveFormsModule, MatDialogModule],
      providers : [CookieService,
      {
        provide: AccountService,
        useValue: {
          currentUser: of({email: "e@e.com", forename:"a", surname: "e"})
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
    fixture = TestBed.createComponent(EditProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it("should disable submit when a field is empty", () => {
    component.editForm.get("forename").setValue("");
    fixture.detectChanges();

    let buttonDisabled = fixture.debugElement.query(By.css("#submit")).nativeElement.disabled;
    expect(buttonDisabled).toBeTruthy();
    
  });
  it("should enable submit if all fields are valid", () =>  {
    let buttonDisabled = fixture.debugElement.query(By.css("#submit")).nativeElement.disabled;
    expect(buttonDisabled).toBeFalsy();
  });
  it("should disable submit if email is invalid", () => {
    component.editForm.get("email").setValue("invalidemailcom");
    fixture.detectChanges();

    let buttonDisabled = fixture.debugElement.query(By.css("#submit")).nativeElement.disabled;
    expect(buttonDisabled).toBeTruthy();
  });
});

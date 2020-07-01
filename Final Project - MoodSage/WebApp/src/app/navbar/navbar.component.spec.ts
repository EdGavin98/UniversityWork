import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { NavbarComponent } from './navbar.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { CookieService } from 'ngx-cookie-service';
import { By } from '@angular/platform-browser';

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NavbarComponent],
      imports: [HttpClientTestingModule],
      providers : [CookieService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('logged out should display log in/register', () => {
    component.loggedIn = false
    fixture.detectChanges()
    const register = fixture.debugElement.query(By.css("#register"))
    const logout = fixture.debugElement.query(By.css("#logout"))
    const login = fixture.debugElement.query(By.css("#login"))

    expect(login).toBeTruthy();
    expect(register).toBeTruthy();
    expect(logout).toBeFalsy();

  })
  it('logged in should display log out', () => {
    component.loggedIn = true
    fixture.detectChanges()
    const register = fixture.debugElement.query(By.css("#register"))
    const logout = fixture.debugElement.query(By.css("#logout"))
    const login = fixture.debugElement.query(By.css("#login"))

    expect(login).toBeFalsy();
    expect(register).toBeFalsy();
    expect(logout).toBeTruthy();
  })

});

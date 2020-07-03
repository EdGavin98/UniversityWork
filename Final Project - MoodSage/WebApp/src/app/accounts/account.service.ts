import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { CookieService } from 'ngx-cookie-service';
import { Observable, BehaviorSubject } from 'rxjs';
import { Profile } from './profile';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private _userLoggedIn : BehaviorSubject<boolean>;
  public userLoggedIn : Observable<boolean>;

  private _currentUser : BehaviorSubject<Profile>
  public currentUser : Observable<Profile>


  constructor(private http : HttpClient, private cookieService : CookieService) { 
    this._userLoggedIn = new BehaviorSubject(false);
    this.getLoginStatus()
    this.userLoggedIn = this._userLoggedIn.asObservable();

    this._currentUser = new BehaviorSubject(null);
    this.currentUser = this._currentUser.asObservable(); //Init with a null value in case anything tries to subscribe before the profile is retrieved from the network
  }

  private uri = "https://api.stsoft.tech/";
  private options = {
    headers : new HttpHeaders({"Authorization" : `Bearer ${this.getToken()}`})
  };

  registerAccount(forename : String, surname : String, email : String, password : String) {
    
    var user = {
      forename : forename,
      surname : surname,
      email : email,
      password : password,
      role : "therapist"
    }

    return this.http.post(this.uri + "auth/register", user, { observe : "response"});
    
  }

  editAccount(forename: String, surname: String, email: String) {
    var newDetails = {
      forename: forename,
      surname: surname,
      email: email
    }

    return this.http.put(this.uri + "api/profile", newDetails, this.options);
  }

  deleteAccount() {
    return this.http.delete(this.uri + "api/profile", this.options);
  }

  login(email : String, password : String) {
    var login = {
      email : email,
      password : password
    }

    return this.http.post(this.uri + "auth/login", login, { observe : "response"}).pipe(
      map(response => {
        var token = response.body['token'];
        this.cookieService.set("userToken", token, 7);
        this._userLoggedIn.next(token);
        this.options.headers = new HttpHeaders({"Authorization" : `Bearer ${this.getToken()}`})
        return response;
      })
    )
  }

  getToken() {
    return this.cookieService.get("userToken");
  }

  /**
  * Function to check the log in status of the user, 
  * to be run when the application starts up to see if there is already a cookie
  */
  getLoginStatus() {
    var cookie = this.cookieService.get("userToken");
    if (cookie != null && cookie !== "") {
      this._userLoggedIn.next(true);
    } else {
      this._userLoggedIn.next(false);
    }
  }

  logOut() {
    this.cookieService.delete("userToken");
    this._userLoggedIn.next(false);
    this._currentUser.next(null);
  }

   getProfile() {
    this.http.get(`${this.uri}api/profile`, this.options).subscribe(
      (response : Profile) => {
        this._currentUser.next(response)
      },
      (error : HttpErrorResponse) => {
        console.log(error)
        this._currentUser.next(null)
      }   
    )
  }



}

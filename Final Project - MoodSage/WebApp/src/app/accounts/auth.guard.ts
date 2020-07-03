import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { AccountService } from './account.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AccountService, private router: Router) {
    this.setLoginStatus()
  }

  loginStatus = false
  
  canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    if (this.loginStatus) {
      return true
    } else {
      this.router.navigate(['/'])
      return false
    }
  }

  
  private setLoginStatus() {
    this.authService.userLoggedIn.subscribe(
      (status : boolean) => {
        if (status != null) {
           this.loginStatus = status
        }
      }
    )
  }

}

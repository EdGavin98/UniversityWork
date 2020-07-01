import { Component, OnInit } from '@angular/core';
import { AccountService } from '../accounts/account.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  constructor(private accountService : AccountService) { }

  loggedIn : Boolean

  ngOnInit(): void {

    this.accountService.getLoginStatus();
    this.accountService.userLoggedIn.subscribe(status => {
      if (status != null)  {
        console.log(status)
        this.loggedIn = status;
      }
    });

  }

  logOut() {
    this.accountService.logOut();
  }

}

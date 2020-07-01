import { Component, OnInit } from '@angular/core';
import { AccountService } from '../accounts/account.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(
    private accountService: AccountService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.accountService.userLoggedIn.subscribe(status => {
      if (status) {
        this.router.navigate(['/dashboard'])
      }
    })
  }

}

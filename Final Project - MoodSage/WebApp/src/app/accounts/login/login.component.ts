import { Component, OnInit } from '@angular/core';
import { AccountService } from '../account.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormGroup, FormControl, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(
    private accountService : AccountService,
     private router : Router,
     private snackBar : MatSnackBar ) { }

  email = new FormControl('', [Validators.required])
  password = new FormControl('', [Validators.required]);

  loginForm = new FormGroup({
    email: this.email,
    password: this.password
  })

  ngOnInit(): void {
  }

  onSubmit() {
    this.accountService.login(this.email.value, this.password.value).subscribe(
      response => {
        if (response.status == 200) {
          this.router.navigate(["/dashboard"]);
        } else {
          this.snackBar.open("Incorrect username/password");
        }
      },
      error => {
        this.snackBar.open("Incorrect username/password");
      }
    )  
  }

}

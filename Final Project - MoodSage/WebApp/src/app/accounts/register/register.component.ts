import { Component, OnInit } from '@angular/core';
import { AccountService } from '../account.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { FormControl, Validators, FormGroup, ValidationErrors, FormBuilder } from '@angular/forms';
@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  constructor(
    private accountService : AccountService,
    private router : Router,
    private snackBar : MatSnackBar,
    private builder: FormBuilder) { 
      
    }

  registerForm = this.builder.group({
    'forename': new FormControl('', [Validators.required]),
    'surname': new FormControl('', [Validators.required]),
    'email': new FormControl('', [Validators.required, Validators.email]),
    'password': new FormControl('', [Validators.required, Validators.pattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$"), Validators.minLength(8)]),
    'passwordConfirm': new FormControl('',[Validators.required, Validators.pattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9]).{8,}$"), Validators.minLength(8)])
  },
  { 
    validator: this.passwordsMatch
  })
  
  ngOnInit(): void {

  }

  onSubmit(): void {
    this.accountService.registerAccount(
      this.registerForm.get('forename').value,
      this.registerForm.get('surname').value, 
      this.registerForm.get('email').value, 
      this.registerForm.get('password').value
      ).subscribe(
      response => {
        if (response.status == 200) {
          alert("Account creation successful");
          this.router.navigate(["/login"]);
        }
    },
    error => {
      this.snackBar.open("An error occured, please check your details or network settings")
      });
    }

  private passwordsMatch(group: FormGroup): ValidationErrors {

    let pass = group.get('password').value;
    let cPass = group.get('passwordConfirm').value;
    if (pass !== cPass) {
      return {notSame: true}
    } else {
      return null
    }
  }

  
}

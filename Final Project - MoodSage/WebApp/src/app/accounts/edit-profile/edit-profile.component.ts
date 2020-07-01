import { Component, OnInit } from '@angular/core';
import { AccountService } from '../account.service';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogData } from 'src/app/utils/confirm-dialog/confirm-dialog-data';
import { ConfirmDialogComponent } from 'src/app/utils/confirm-dialog/confirm-dialog.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  constructor(
    private snackBar: MatSnackBar,
    private accountService: AccountService,
    private builder: FormBuilder,
    private dialog: MatDialog,
    private router: Router
  ) { }

  editForm = this.builder.group({
    'forename': new FormControl('', [Validators.required]),
    'surname': new FormControl('', [Validators.required]),
    'email': new FormControl('', [Validators.required, Validators.email]),
  })

  ngOnInit(): void {
    this.accountService.currentUser.subscribe(user => {
      this.editForm.get("forename").setValue(user.forename);
      this.editForm.get("surname").setValue(user.surname);
      this.editForm.get("email").setValue(user.email);
    })
  }

  onSubmit(): void {
    this.accountService.editAccount(
      this.editForm.get('forename').value,
      this.editForm.get('surname').value, 
      this.editForm.get('email').value, 
      ).subscribe(
      response => {
        this.snackBar.open("Profile successfully updated")
        this.accountService.getProfile();
    },
      error => {
        this.snackBar.open("An error occured, please check your details or network settings")
    });
  }

  onDelete(): void {
    const confirm: ConfirmDialogData = {
      title: "Are you sure?",
      message: "Are you sure you want to delete your account? This action can not be undone"
    }

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {height: "600", width: "200", data: confirm})
    dialogRef.afterClosed().subscribe((isConfirmed: Boolean) => {
      if (isConfirmed) {
        this.accountService.deleteAccount().subscribe(
          response => {
            this.accountService.logOut();
            this.router.navigate(['/']);
          },
          error => {
            this.snackBar.open("Error while removing account")
          }
        )
      } else {
        this.snackBar.open("Cancelled account deletion")
      }
    })

  }

}

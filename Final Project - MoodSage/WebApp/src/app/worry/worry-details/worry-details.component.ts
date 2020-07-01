import { Component, OnInit, Inject } from '@angular/core';
import { MatBottomSheetRef, MAT_BOTTOM_SHEET_DATA } from '@angular/material/bottom-sheet';
import { DateUtils } from 'src/app/utils/date-utils';
@Component({
  selector: 'app-worry-details',
  templateUrl: './worry-details.component.html',
  styleUrls: ['./worry-details.component.css']
})
export class WorryDetailsComponent implements OnInit {

  constructor(
    private bottomSheetRef: MatBottomSheetRef<WorryDetailsComponent>, 
    @Inject(MAT_BOTTOM_SHEET_DATA) public worry: any
  ) { }

  getDate(): string {
    return DateUtils.formatDateWithTime(new Date(this.worry.date))
  }

  ngOnInit(): void {
  }

}

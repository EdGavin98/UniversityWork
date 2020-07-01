import { Component, OnInit, Inject } from '@angular/core';
import { MatBottomSheetRef, MAT_BOTTOM_SHEET_DATA } from '@angular/material/bottom-sheet';
import { DateUtils } from 'src/app/utils/date-utils';

@Component({
  selector: 'app-mood-details',
  templateUrl: './mood-details.component.html',
  styleUrls: ['./mood-details.component.css']
})
export class MoodDetailsComponent implements OnInit {

  constructor(
    private _bottomSheetRef: MatBottomSheetRef<MoodDetailsComponent>,
    @Inject(MAT_BOTTOM_SHEET_DATA) public mood: any) { }

  ngOnInit(): void {
  }

  getDate(): string {
    return DateUtils.formatDate(new Date(this.mood.date));
  }


}

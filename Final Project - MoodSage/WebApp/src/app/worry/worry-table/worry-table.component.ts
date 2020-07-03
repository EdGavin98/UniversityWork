import { Component, OnInit, Input } from '@angular/core';
import { WorryStatsHelper } from 'src/app/utils/stats/worry-stats-helper';
import { justThisMonth } from 'src/app/utils/functions/justThisMonth';
import { Worry } from '../worry';

@Component({
  selector: 'app-worry-table',
  templateUrl: './worry-table.component.html',
  styleUrls: ['./worry-table.component.css']
})
export class WorryTableComponent implements OnInit {

  constructor() { }
  @Input() worries: Worry[] = [];

  data: {}[]
  displayedColumns: string[] = ['day', 'averageAllTime', 'averageThisMonth'];

  ngOnInit(): void {
    this.setData()
  }

  private setData() {
    const averageByDayAllTime = WorryStatsHelper.getAverageSeverityByDay(this.worries)
    const averageByDayThisMonth = WorryStatsHelper.getAverageSeverityByDay(justThisMonth(this.worries))

    this.data = [
      {day: 'Monday', averageAllTime: averageByDayAllTime[0], averageThisMonth: averageByDayThisMonth[0]},
      {day: 'Tuesday', averageAllTime: averageByDayAllTime[1], averageThisMonth: averageByDayThisMonth[1]},
      {day: 'Wednesday', averageAllTime: averageByDayAllTime[2], averageThisMonth: averageByDayThisMonth[2]},
      {day: 'Thursday', averageAllTime: averageByDayAllTime[3], averageThisMonth: averageByDayThisMonth[3]},
      {day: 'Friday', averageAllTime: averageByDayAllTime[4], averageThisMonth: averageByDayThisMonth[4]},
      {day: 'Saturday', averageAllTime: averageByDayAllTime[5], averageThisMonth: averageByDayThisMonth[5]},
      {day: 'Sunday', averageAllTime: averageByDayAllTime[6], averageThisMonth: averageByDayThisMonth[6]},
    ];
   }

}

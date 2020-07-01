import {Component, Input, OnInit} from '@angular/core';
import { MoodStatsHelper } from 'src/app/utils/stats/mood-stats-helper';
import { justThisMonth } from 'src/app/utils/functions/justThisMonth';
import { Mood } from '../mood';

@Component({
  selector: 'app-mood-table',
  templateUrl: './mood-table.component.html',
  styleUrls: ['./mood-table.component.css']
})
export class MoodTableComponent implements OnInit {

  @Input() moods: Mood[] = [];

  constructor() { }
  data: {}[];
  displayedColumns: string[] = ['day', 'averageAllTime', 'averageThisMonth'];

  ngOnInit(): void {
    this.setData()
  }

  setData() {
    const averageByDayAllTime = MoodStatsHelper.getAverageRatingByDay(this.moods)
    const averageByDayThisMonth = MoodStatsHelper.getAverageRatingByDay(justThisMonth(this.moods))

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

import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {MoodStatsHelper} from '../../utils/stats/mood-stats-helper';
import {Mood} from '../../mood/mood';
import {Worry} from '../../worry/worry';
import {WorryStatsHelper} from '../../utils/stats/worry-stats-helper';
import { BaseChartDirective } from 'ng2-charts';
import { justThisMonth } from 'src/app/utils/functions/justThisMonth';

@Component({
  selector: 'app-rating-by-day-chart',
  templateUrl: './rating-by-day-chart.component.html',
  styleUrls: ['./rating-by-day-chart.component.css']
})
export class RatingByDayChartComponent implements OnInit {

  constructor() { }

  @Input() moods: Mood[] = [];
  @Input() worries: Worry[] = [];
  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective;

  thisMonthOnly = false
  data: any[];
  labels = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];
  chartType = 'bar';
  options = {
    scales : {
      yAxes : [{
        ticks : {
          suggestedMin : 1,
          suggestedMax : 10
        }
      }]
    }
  }

  ngOnInit(): void {
    this.setData();
  }

  private setData() {
    let moodData: number[];
    let worryData: number[];

    if (this.thisMonthOnly) {
      moodData = MoodStatsHelper.getAverageRatingByDay(justThisMonth(this.moods))
      worryData = WorryStatsHelper.getAverageSeverityByDay(justThisMonth(this.worries))
    } else {
      moodData = MoodStatsHelper.getAverageRatingByDay(this.moods)
      worryData = WorryStatsHelper.getAverageSeverityByDay(this.worries)
    }

    this.data = [
      {
        data : moodData,
        label : 'Moods'
      },
      {
        data :  worryData,
        label : 'Worries'
      }
    ];
  }

  onToggleChanged() {
    this.setData()
    this.chart.update()
  }

}

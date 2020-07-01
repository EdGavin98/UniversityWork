import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { WorryStatsHelper } from 'src/app/utils/stats/worry-stats-helper';
import { Worry } from 'src/app/worry/worry';
import { justThisMonth } from 'src/app/utils/functions/justThisMonth';
import { BaseChartDirective } from 'ng2-charts';

@Component({
  selector: 'app-worry-each-day-chart',
  templateUrl: './worry-each-day-chart.component.html',
  styleUrls: ['./worry-each-day-chart.component.css']
})
export class WorryEachDayChartComponent implements OnInit {

  constructor() { }

  @Input() worries: Worry[] = [];
  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective;

  thisMonthOnly = false

  datasets: {}[] = [];
  chartType = "line";
  options = {
    elements: {
      line: {
          tension: 0, // disables bezier curves
      }
    },
    scales : {
      xAxes : [{
        type : 'time',
        distribution: 'linear',
        time: {
          minUnit: 'day',
          displayFormats: {
              month: 'MMM YYYY'
          }
        }
      }],
      yAxes : [{
        stepSize: 1,
        ticks : {
          suggestedMin: 0
        }
      }]
    } 
  }

  ngOnInit(): void {
    this.setData();
  }

  private setData() {
    let chartData;
    if (this.thisMonthOnly) {
         chartData = WorryStatsHelper.getNumWorriesPerDay(justThisMonth(this.worries));
    } else {
        chartData = WorryStatsHelper.getNumWorriesPerDay(this.worries);
    }
    this.datasets = [{
      label: "Number Per Day",
      data: chartData
    }]
  }

  onChanged(): void {
    this.setData();
    this.chart.update();
  }

}

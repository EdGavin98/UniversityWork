import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Worry } from 'src/app/worry/worry';
import { WorryStatsHelper } from 'src/app/utils/stats/worry-stats-helper';
import { BaseChartDirective } from 'ng2-charts';
import { justThisMonth } from 'src/app/utils/functions/justThisMonth';

@Component({
  selector: 'app-worries-with-solutions-chart',
  templateUrl: './worries-with-solutions-chart.component.html',
  styleUrls: ['./worries-with-solutions-chart.component.css']
})
export class WorriesWithSolutionsChartComponent implements OnInit {

  constructor() { }

  @Input() worries: Worry[] = [];
  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective;

  selected = "All"
  thisMonthOnly = false;

  data: number[] = [];
  labels: string[] = [];
  chartType = "pie";

  ngOnInit(): void {
    this.setData()
  }

  setData() {
    let countWith: number;
    let toDisplay: Worry[]

    if (this.selected === "All") {
      toDisplay = this.worries
    } else {
      toDisplay = this.worries.filter((worry: Worry) => {
        return worry.type == this.selected
      })
    }

    if (this.thisMonthOnly) {
      countWith = WorryStatsHelper.getCountWithSolutions(justThisMonth(toDisplay));
    } else {
      countWith = WorryStatsHelper.getCountWithSolutions(toDisplay);
    }

    this.data = [
      countWith,
      this.worries.length - countWith
    ]

    this.labels = [
      "With Solutions",
      "Without Solutions"
    ]
  }

  onChanged() {
    this.setData();
    this.chart.update();
  }

}

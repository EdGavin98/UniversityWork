import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {Worry} from '../../worry/worry';
import {WorryStatsHelper} from '../../utils/stats/worry-stats-helper';
import { BaseChartDirective } from 'ng2-charts';
import { justThisMonth } from 'src/app/utils/functions/justThisMonth';

@Component({
  selector: 'app-worry-type-chart',
  templateUrl: './worry-type-chart.component.html',
  styleUrls: ['./worry-type-chart.component.css']
})
export class WorryTypeChartComponent implements OnInit {

  constructor() { }

  @Input() worries: Worry[] = [];
  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective;

  thisMonthOnly = false
  data: number[];
  labels = ['Current', 'Hypothetical'];
  chartType = 'pie';


  ngOnInit(): void {
    this.setData();
  }

  private setData(): void {
    if (this.thisMonthOnly) {
      this.data = WorryStatsHelper.getHypotheticalAndCurrentCount(justThisMonth(this.worries));
    } else {
      this.data = WorryStatsHelper.getHypotheticalAndCurrentCount(this.worries);
    }
  }

  onToggleChanged() {
    this.setData()
    this.chart.update()
  }

}

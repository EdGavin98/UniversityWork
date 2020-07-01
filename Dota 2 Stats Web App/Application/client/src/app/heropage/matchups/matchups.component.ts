import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-matchups',
  templateUrl: './matchups.component.html',
  styleUrls: ['./matchups.component.css']
})
export class MatchupsComponent implements OnInit {

  constructor() { }
  @Input() matchups : any;

  hasData = false;
  labels : [];
  values : any;
  chartType : String;

  barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales: {
      yAxes : [{
        barThickness: 15,
        ticks : {
          autoSkip: false,
          stepSize: 20
        }
      }], 

      xAxes : [{
          ticks: {
            suggestedMin: 0,
            suggestedMax: 100
        }
        }]
      },

    dataset: {
      barThickness: 15
    }
  };

  ngOnInit() {

    if (this.matchups != null && this.matchups != undefined) {
      this.labels = this.matchups.heronames;
      this.values = [{ data: this.matchups.winrates, label : "Winrates Vs Other Heroes" }];
      this.chartType = "horizontalBar";
      this.hasData = true;
    } else {
      this.hasData = false;
    }
    
  }

}

import { Component, OnInit, Input } from '@angular/core';
import { MatchService } from 'src/app/match.service';

@Component({
  selector: 'app-winrates',
  templateUrl: './winrates.component.html',
  styleUrls: ['./winrates.component.css']
})
export class WinratesComponent implements OnInit {

  constructor() { }

  @Input() data;

  public hasData = false;
  public labels = [];

  public values : any;
  public chartType : String;

  public barChartOptions = {
    scaleShowVerticalLines: false,
    responsive: true,
    scales : {
      yAxes: [{
        scaleLabel: {
          display: true,
          labelString: 'Winrate'
        },
        ticks : {
          suggestedMin: 0,
          suggestedMax: 100
        }
      }
    ],
  
      xAxes: [{
        scaleLabel: {
          display: true,
          labelString: 'Time (Minutes)'
        }
      }]
    }
  }

  ngOnInit() {
    if (this.data != null || this.data != undefined) {
      this.values = [
        {
        data : this.data.winrates, 
        label : "Hero winrate over match length"
        }
      ]   

      this.labels = this.data.durations;
      this.hasData = true;

    } else {
      this.hasData = false;
    }
    
    this.chartType = "line";
  }
}
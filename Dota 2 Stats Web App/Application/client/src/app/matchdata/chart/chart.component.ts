import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {

  constructor() { }
  @Input() xp;
  @Input() gold;

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
          labelString: 'Advantage'
        }}],
  
      xAxes: [{
        scaleLabel: {
          display: true,
          labelString: 'Time (Minutes)'
        }}]
    }
  }

  ngOnInit() {
    if (this.xp != null && this.gold != null) {
      this.values = [
        {
        data : this.xp, 
        label : " Radiant Xp Advantage"
        },
        {
          data : this.gold,
          label : "Radiant Gold Advantage"
        }
     ] 

     var length = this.xp.length >= this.gold.length ? this.xp.length : this.gold.length;

     for (var i = 1; i <= length; i++) {
       this.labels.push(i);
     }

     this.hasData = true;
    } else {
      this.hasData = false;
    }
    
    this.chartType = "line";
  }

}

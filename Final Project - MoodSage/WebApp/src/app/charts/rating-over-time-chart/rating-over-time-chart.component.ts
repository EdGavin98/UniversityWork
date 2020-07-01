import { Component, OnInit, Input, ViewChild } from '@angular/core';
import { Mood } from 'src/app/mood/mood';
import { Worry } from 'src/app/worry/worry';
import { filterOutliers } from 'src/app/utils/functions/filterOutliers';
import { justThisMonth } from 'src/app/utils/functions/justThisMonth';
import { BaseChartDirective } from 'ng2-charts';
import { MatBottomSheet } from '@angular/material/bottom-sheet';
import { WorryDetailsComponent } from 'src/app/worry/worry-details/worry-details.component';
import { MoodDetailsComponent } from 'src/app/mood/mood-details/mood-details.component';
import * as pluginAnnotations from 'chartjs-plugin-annotation';

@Component({
  selector: 'app-rating-over-time-chart',
  templateUrl: './rating-over-time-chart.component.html',
  styleUrls: ['./rating-over-time-chart.component.css']
})
export class RatingOverTimeChartComponent implements OnInit {

  constructor(private bottomSheet: MatBottomSheet) { }

  @Input() moods: Mood[] = [];
  @Input() worries: Worry[] = [];
  @Input() moodTarget: number;
  @Input() worryTarget: number;
  @ViewChild(BaseChartDirective, { static: true }) chart: BaseChartDirective;

  thisMonthOnly = false;
  noFilter = false;
  showMoodTarget = false;
  showWorryTarget = false;

  plugins = [pluginAnnotations]
  labels : String[] = [];
  data: any[] = [];
  chartType = 'line';
  options = {
    elements: {
      line: {
          tension: 0, // disables bezier curves
      }
    },
    hover: {
      mode: 'nearest'
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
          suggestedMin: 1,
          suggestedMax: 10
        }
      }]
    },
    annotation : {
      annotations : []
    }
  }

  ngOnInit(): void {
    this.worries.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
    this.moods.sort((a, b) => new Date(a.date).getTime() - new Date(b.date).getTime());
    this.setData();
  }

  setData() {
    let moodData: Mood[]
    let worryData: Worry[]
    if (this.thisMonthOnly) {
      moodData = this.getMoodData(justThisMonth(this.moods))
      worryData = this.getWorryData(justThisMonth(this.worries))
    } else {
      moodData = this.getMoodData(this.moods)
      worryData = this.getWorryData(this.worries)
    }


    this.data = [
      {
        label: "Moods",
        data: moodData
      },
      {
        label: "Worries",
        data: worryData
      }
    ]
  }

  private getWorryData(data: Worry[]): any {    
    let chartData = []

    for (const worry of data) {
      const date = new Date(worry.date)

      chartData.push({
        t: date,
        y: worry.severity 
      })
    }

    if (this.noFilter) {
      return chartData
    } else {
      return filterOutliers(chartData)
    }
  }

  private getMoodData(data : Mood[]): any {
    let chartData = []
    for (const mood of data) {
      const date = new Date(mood.date)

      chartData.push({
        t: date,
        y: mood.rating
      })
    }

    if (this.noFilter) {
      return chartData
    } else {
      return filterOutliers(chartData)
    }
  }

  onAnnotationToggleChange(type : string) {
    let annotationValue: number;
    let annotationLabel: string;
    let show: boolean;
    if (type === "Mood") {
      annotationValue = this.moodTarget
      annotationLabel = "Mood Target"
      show = this.showMoodTarget
    } else {
      annotationValue = this.worryTarget
      annotationLabel = "Worry Target"
      show = this.showWorryTarget
    }
    if (show) {
        this.options.annotation.annotations.push({
        id: type,
        type: 'line',
        mode: 'horizontal',
        scaleID: 'y-axis-0',
        value: annotationValue,
        borderColor: 'orange',
        borderWidth: 2,
        label: {
          enabled: true,
          fontColor: 'orange',
          content: annotationLabel
        }
      })
    } else {
      this.options.annotation.annotations = this.options.annotation.annotations.filter(annotation => {
        return annotation.id !== type
      })
      console.log(this.options.annotation.annotations)
    }

    this.chart.ngOnChanges({})
  }

  onToggleChange() {
    this.setData()
    this.chart.update()
  }

  chartClicked({event, active}) {
    console.log(event)
    if (active[0] != undefined) {
      this.getSpecificElement(active[0]._datasetIndex, active[0]._index)
    }
  }

  getSpecificElement(dataset: number, index: number) {
      const clickedDate = this.data[dataset].data[index].t
      if (dataset == 0) {
          let data = this.moods.find((mood: Mood) => {
            let toCheck = new Date(mood.date)
            return toCheck.getTime() === clickedDate.getTime()
          })
          this.bottomSheet.open(MoodDetailsComponent, {data})
      } else {
          let data = this.worries.find((worry: Worry) => {
            let toCheck = new Date(worry.date)
            return toCheck.getTime() === clickedDate.getTime()
          })
          this.bottomSheet.open(WorryDetailsComponent, {data})
      }
  }
      


  

}

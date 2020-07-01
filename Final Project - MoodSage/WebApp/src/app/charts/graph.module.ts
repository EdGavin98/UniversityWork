import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RatingByDayChartComponent } from './rating-by-day-chart/rating-by-day-chart.component';
import { ChartsModule } from 'ng2-charts';
import { RatingOverTimeChartComponent } from './rating-over-time-chart/rating-over-time-chart.component';
import { MatSlideToggleModule } from '@angular/material/slide-toggle'
import { MatRadioModule } from '@angular/material/radio'
import { FormsModule } from '@angular/forms'
import { WorryTypeChartComponent } from './worry-type-chart/worry-type-chart.component';
import { MatCardModule } from '@angular/material/card';
import { WorriesWithSolutionsChartComponent } from './worries-with-solutions-chart/worries-with-solutions-chart.component';
import { MatSelectModule } from '@angular/material/select';
import { WorryEachDayChartComponent } from './worry-each-day-chart/worry-each-day-chart.component';

@NgModule({
  declarations: [RatingByDayChartComponent, RatingOverTimeChartComponent, WorryTypeChartComponent, WorriesWithSolutionsChartComponent, WorryEachDayChartComponent],
  exports: [
    RatingByDayChartComponent,
    RatingOverTimeChartComponent,
    WorryTypeChartComponent,
    WorriesWithSolutionsChartComponent,
    WorryEachDayChartComponent
  ],
  imports: [
    CommonModule,
    ChartsModule,
    MatRadioModule,
    FormsModule,
    MatSlideToggleModule,
    MatCardModule,
    MatSelectModule
  ]
})
export class GraphModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ScoreboardComponent } from './scoreboard/scoreboard.component';
import { MatchdataComponent } from './matchdata/matchdata.component';
import { TeamdataComponent } from './teamdata/teamdata.component';
import { CommentsComponent } from './comments/comments.component';
import { FormsModule } from '@angular/forms';
import { ChartComponent } from './chart/chart.component';
import { ChartsModule } from 'ng2-charts';


@NgModule({
  declarations: [ScoreboardComponent, MatchdataComponent, TeamdataComponent, CommentsComponent, ChartComponent],
  imports: [
    CommonModule,
    FormsModule,
    ChartsModule
  ],
  exports: [
    MatchdataComponent
  ]
})

export class MatchdataModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChartsModule } from 'ng2-charts';
import { HeropageComponent } from './heropage/heropage.component';
import { MatchupsComponent } from './matchups/matchups.component';
import { RecentMatchesComponent } from './recent-matches/recent-matches.component';
import { WinratesComponent } from './winrates/winrates.component';



@NgModule({
  declarations: [HeropageComponent, MatchupsComponent, RecentMatchesComponent, WinratesComponent],
  imports: [
    CommonModule,
    ChartsModule
  ],
  exports: [
    HeropageComponent
  ]
})
export class HeropageModule { }

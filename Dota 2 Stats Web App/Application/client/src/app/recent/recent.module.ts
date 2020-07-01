import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatchListComponent } from './match-list/match-list.component';

@NgModule({
  declarations: [MatchListComponent],
  imports: [
    CommonModule
  ],
  exports: [
    MatchListComponent
  ]
})
export class RecentModule { }

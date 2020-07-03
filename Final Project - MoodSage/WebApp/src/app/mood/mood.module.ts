import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MoodTableComponent } from './mood-table/mood-table.component';
import { MoodListComponent } from './mood-list/mood-list.component';
import { MatTableModule } from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MoodDetailsComponent } from './mood-details/mood-details.component';


@NgModule({
  declarations: [MoodTableComponent, MoodListComponent, MoodDetailsComponent],
  exports: [
    MoodTableComponent,
    MoodListComponent
  ],
  imports: [
    CommonModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule
  ],
  entryComponents: [MoodDetailsComponent]
})
export class MoodModule { }

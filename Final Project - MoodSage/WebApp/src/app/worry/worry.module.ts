import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorryTableComponent } from './worry-table/worry-table.component';
import { WorryListComponent } from './worry-list/worry-list.component';
import { MatTableModule } from '@angular/material/table';
import {MatPaginatorModule} from '@angular/material/paginator';
import { ChartsModule } from 'ng2-charts';
import { MatSortModule } from '@angular/material/sort';
import { MatIconModule } from '@angular/material/icon'; 
import { WorryDetailsComponent } from './worry-details/worry-details.component';
import { MatBottomSheetModule } from '@angular/material/bottom-sheet';
import { MatButtonModule } from '@angular/material/button';
import { WorrySolutionSectionComponent } from './worry-solution-section/worry-solution-section.component';



@NgModule({
  declarations: [WorryTableComponent, WorryListComponent, WorryDetailsComponent, WorrySolutionSectionComponent],
  exports: [
    WorryTableComponent,
    WorryListComponent
  ],
  imports: [
      CommonModule,
      MatTableModule,
      MatPaginatorModule,
      MatSortModule,
      ChartsModule,
      MatBottomSheetModule,
      MatIconModule,
      MatButtonModule
  ],
  entryComponents: [
    WorryDetailsComponent
  ]
})
export class WorryModule { }

import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HerogridComponent } from './herogrid/herogrid.component';
import { FormsModule } from '@angular/forms'



@NgModule({
  declarations: [HerogridComponent],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [
    HerogridComponent
  ]
})
export class HeroesModule { }

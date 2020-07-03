import { Component, OnInit, Input } from '@angular/core';
import { Solution } from '../solution';

@Component({
  selector: 'app-worry-solution-section',
  templateUrl: './worry-solution-section.component.html',
  styleUrls: ['./worry-solution-section.component.css']
})
export class WorrySolutionSectionComponent implements OnInit {

  constructor() { }

  @Input() solutions: Solution[] = [];
  currentIndex: number;

  ngOnInit(): void {
    this.currentIndex = 0;
  }
  
  onLeftClicked() {
    if (this.currentIndex > 0 ) {
      this.currentIndex--;
    }
  }

  onRightClicked() {
    if (this.currentIndex < this.solutions.length - 1 ) {
      this.currentIndex++;
    }
  }
 
}

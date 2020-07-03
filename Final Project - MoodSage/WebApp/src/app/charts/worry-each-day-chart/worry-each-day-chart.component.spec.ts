import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorryEachDayChartComponent } from './worry-each-day-chart.component';

describe('WorryEachDayChartComponent', () => {
  let component: WorryEachDayChartComponent;
  let fixture: ComponentFixture<WorryEachDayChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorryEachDayChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorryEachDayChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RatingByDayChartComponent } from './rating-by-day-chart.component';

describe('RatingByDayChartComponent', () => {
  let component: RatingByDayChartComponent;
  let fixture: ComponentFixture<RatingByDayChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RatingByDayChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RatingByDayChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

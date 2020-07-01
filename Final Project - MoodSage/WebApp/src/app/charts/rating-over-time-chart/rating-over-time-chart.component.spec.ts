import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RatingOverTimeChartComponent } from './rating-over-time-chart.component';
import { MatBottomSheetModule } from '@angular/material/bottom-sheet';

describe('RatingOverTimeChartComponent', () => {
  let component: RatingOverTimeChartComponent;
  let fixture: ComponentFixture<RatingOverTimeChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RatingOverTimeChartComponent ],
      imports : [MatBottomSheetModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RatingOverTimeChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorryTypeChartComponent } from './worry-type-chart.component';

describe('WorryTypeChartComponent', () => {
  let component: WorryTypeChartComponent;
  let fixture: ComponentFixture<WorryTypeChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorryTypeChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorryTypeChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

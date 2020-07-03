import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorriesWithSolutionsChartComponent } from './worries-with-solutions-chart.component';

describe('WorriesWithSolutionsChartComponent', () => {
  let component: WorriesWithSolutionsChartComponent;
  let fixture: ComponentFixture<WorriesWithSolutionsChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorriesWithSolutionsChartComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorriesWithSolutionsChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

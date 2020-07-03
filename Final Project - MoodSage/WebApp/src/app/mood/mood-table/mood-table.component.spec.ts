import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MoodTableComponent } from './mood-table.component';
import { Mood } from '../mood';
import { MatTableModule } from '@angular/material/table';
import { By } from '@angular/platform-browser';

describe('MoodTableComponent', () => {
  let component: MoodTableComponent;
  let fixture: ComponentFixture<MoodTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MoodTableComponent ],
      imports : [MatTableModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MoodTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it("should have seven rows plus title with no data input", () => {
    const tableRows = fixture.debugElement.queryAll(By.css("tr")).length;
    expect(tableRows).toBe(8);
  });
  it("should have seven rows plus title with data input", () => {
    let worries : Mood[] = [
      {date: new Date(), rating: 3, comment: "test"},
      {date: new Date(), rating: 4, comment: "test"},
      {date: new Date(), rating: 5, comment: "test"},
      {date: new Date(), rating: 6, comment: "test"},
      {date: new Date(), rating: 7, comment: "test"}
    ];
    component.data = worries
    const tableRows = fixture.debugElement.queryAll(By.css("tr")).length;
    console.log(tableRows)
    expect(tableRows).toBe(8);
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MoodListComponent } from './mood-list.component';
import { Mood } from '../mood';
import { By } from '@angular/platform-browser';
import { MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('MoodListComponent', () => {
  let component: MoodListComponent;
  let fixture: ComponentFixture<MoodListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MoodListComponent ],
      imports: [MatTableModule, MatPaginatorModule, MatSortModule, BrowserAnimationsModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MoodListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it("should have one row (title) when given no data", () => {
    const tableRows = fixture.debugElement.queryAll(By.css("tr")).length;
    expect(tableRows).toBe(1);
  });
  
  it("should have 11 rows when given 5 moods", () => { //Hidden description counts as another row, hence 11
    let worries : Mood[] = [
      {date: new Date(), rating: 3, comment: "test"},
      {date: new Date(), rating: 4, comment: "test"},
      {date: new Date(), rating: 5, comment: "test"},
      {date: new Date(), rating: 6, comment: "test"},
      {date: new Date(), rating: 7, comment: "test"}
    ];
    component.moods = worries;
    component.ngOnInit();
    fixture.detectChanges();
    const tableRows = fixture.debugElement.queryAll(By.css("tr")).length;
    expect(tableRows).toBe(11)
  });
});

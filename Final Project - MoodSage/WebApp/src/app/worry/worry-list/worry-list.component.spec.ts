import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorryListComponent } from './worry-list.component';
import { Worry } from '../worry';
import { By } from '@angular/platform-browser';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('WorryListComponent', () => {
  let component: WorryListComponent;
  let fixture: ComponentFixture<WorryListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorryListComponent ],
      imports: [MatTableModule, MatPaginatorModule, MatSortModule, BrowserAnimationsModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorryListComponent);
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
  
  it("should have 11 rows when given 5 worries", () => { //Hidden description counts as another row, hence 11
    let worries : Worry[] = [
      {date: new Date(), severity: 3, type: "Current", description: "test", solutions : []},
      {date: new Date(), severity: 4, type: "Current", description: "test", solutions : []},
      {date: new Date(), severity: 5, type: "Current", description: "test", solutions : []},
      {date: new Date(), severity: 6, type: "Current", description: "test", solutions : []},
      {date: new Date(), severity: 7, type: "Current", description: "test", solutions : []}
    ];
    component.worries = worries;
    component.ngOnInit();
    fixture.detectChanges();
    const tableRows = fixture.debugElement.queryAll(By.css("tr")).length;
    console.log(tableRows)
    expect(tableRows).toBe(11);
  });
});

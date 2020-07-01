import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorryTableComponent } from './worry-table.component';
import { MatTableModule } from '@angular/material/table';
import { By } from '@angular/platform-browser';
import { Worry } from '../worry';

describe('WorryTableComponent', () => {
  let component: WorryTableComponent;
  let fixture: ComponentFixture<WorryTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorryTableComponent ],
      imports : [MatTableModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorryTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should have seven rows plus title with no data input", () => {
    const tableRows = fixture.debugElement.queryAll(By.css("tr")).length;
    expect(tableRows).toBe(8);
  });
  it("should have seven rows plus title with data input", () => {
    let worries : Worry[] = [
      {date: new Date(), severity: 3, type: "Current", description: "test", solutions : []},
      {date: new Date(), severity: 4, type: "Current", description: "test", solutions : []},
      {date: new Date(), severity: 5, type: "Current", description: "test", solutions : []},
      {date: new Date(), severity: 6, type: "Current", description: "test", solutions : []}
    ];
    component.data = worries
    const tableRows = fixture.debugElement.queryAll(By.css("tr")).length;
    console.log(tableRows)
    expect(tableRows).toBe(8);
  });

});

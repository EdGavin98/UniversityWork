import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorryDetailsComponent } from './worry-details.component';
import { MatBottomSheetModule, MatBottomSheetRef, MAT_BOTTOM_SHEET_DATA } from '@angular/material/bottom-sheet';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { By } from '@angular/platform-browser';

describe('WorryDetailsComponent', () => {
  let component: WorryDetailsComponent;
  let fixture: ComponentFixture<WorryDetailsComponent>;

  beforeEach(async(() => {
    let mockBottomSheet = jasmine.createSpyObj(['','',''])
    TestBed.configureTestingModule({
      declarations: [ WorryDetailsComponent],
      imports : [MatBottomSheetModule],
      providers : [{provide: MatBottomSheetRef, useValue: mockBottomSheet }, { provide: MAT_BOTTOM_SHEET_DATA, useValue: {date: new Date(2020, 4, 1), description: "test", severity: 8, type: "Current", solutions: [{description: "", advantages: "", disadvantages: "", timeLogged: new Date()}]}}]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorryDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });
  it("should display data correctly with one solution", () => {

    let dateText = fixture.debugElement.query(By.css("h2")).nativeElement.textContent
    let descriptionText = fixture.debugElement.query(By.css("#description")).nativeElement.textContent
    let severityText = fixture.debugElement.query(By.css(".severity")).nativeElement.textContent
    let typeText = fixture.debugElement.query(By.css(".type")).nativeElement.textContent
    let numberSolutionsText = fixture.debugElement.query(By.css(".numSolutions")).nativeElement.textContent
    let viewSolutionsText = fixture.debugElement.query(By.css("#viewSolutions")).nativeElement.textContent

    expect(dateText).toBe("Friday, 1 May 2020 at 00:00");
    expect(descriptionText).toBe("test");
    expect(severityText).toBe("Severity: 8");
    expect(typeText).toBe("Type: Current");
    expect(numberSolutionsText).toBe("1 solution logged");
    expect(viewSolutionsText).toBe("View it on the details page");
  });
  it("should display data correctly with > 1 solution", () => {
    component.worry.solutions.push({description: "", advantages: "", disadvantages: "", timeLogged: new Date()})
    fixture.detectChanges();
    let dateText = fixture.debugElement.query(By.css("h2")).nativeElement.textContent
    let descriptionText = fixture.debugElement.query(By.css("#description")).nativeElement.textContent
    let severityText = fixture.debugElement.query(By.css(".severity")).nativeElement.textContent
    let typeText = fixture.debugElement.query(By.css(".type")).nativeElement.textContent
    let numberSolutionsText = fixture.debugElement.query(By.css(".numSolutions")).nativeElement.textContent
    let viewSolutionsText = fixture.debugElement.query(By.css("#viewSolutions")).nativeElement.textContent

    expect(dateText).toBe("Friday, 1 May 2020 at 00:00");
    expect(descriptionText).toBe("test");
    expect(severityText).toBe("Severity: 8");
    expect(typeText).toBe("Type: Current");
    expect(numberSolutionsText).toBe("2 solutions logged");
    expect(viewSolutionsText).toBe("View them on the details page");
  });
  it("should display data correctly with 0 solutions", () => {
    component.worry.solutions = []
    fixture.detectChanges();
    let dateText = fixture.debugElement.query(By.css("h2")).nativeElement.textContent
    let descriptionText = fixture.debugElement.query(By.css("#description")).nativeElement.textContent
    let severityText = fixture.debugElement.query(By.css(".severity")).nativeElement.textContent
    let typeText = fixture.debugElement.query(By.css(".type")).nativeElement.textContent
    let numberSolutionsText = fixture.debugElement.query(By.css(".numSolutions")).nativeElement.textContent
    let viewSolutions = fixture.debugElement.query(By.css("#viewSolutions"))

    expect(dateText).toBe("Friday, 1 May 2020 at 00:00");
    expect(descriptionText).toBe("test");
    expect(severityText).toBe("Severity: 8");
    expect(typeText).toBe("Type: Current");
    expect(numberSolutionsText).toBe("0 solutions logged");
    expect(viewSolutions).toBeFalsy();
  
  });
  
});

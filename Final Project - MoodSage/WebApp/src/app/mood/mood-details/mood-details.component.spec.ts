import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MoodDetailsComponent } from './mood-details.component';
import { MatBottomSheetRef, MatBottomSheetModule, MatBottomSheet, MAT_BOTTOM_SHEET_DATA } from '@angular/material/bottom-sheet';
import { By } from '@angular/platform-browser';

describe('MoodDetailsComponent', () => {
  let component: MoodDetailsComponent;
  let fixture: ComponentFixture<MoodDetailsComponent>;

  beforeEach(async(() => {
    let mockBottomRef = jasmine.createSpyObj(['','','']);
    TestBed.configureTestingModule({
      declarations: [ MoodDetailsComponent],
      imports : [MatBottomSheetModule],
      providers : [{provide: MatBottomSheetRef, useValue: mockBottomRef}, {provide: MAT_BOTTOM_SHEET_DATA, useValue: {}} ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MoodDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should display data correctly", () => {
      component.mood = {date: new Date(2020, 4, 1), comment: "test", rating: 8};
      fixture.detectChanges();

      let dateText = fixture.debugElement.query(By.css("h2")).nativeElement.textContent
      let commentText = fixture.debugElement.query(By.css("p")).nativeElement.textContent
      let ratingText = fixture.debugElement.query(By.css("h3")).nativeElement.textContent

      expect(dateText).toBe("Friday, 1 May 2020");
      expect(commentText).toBe("test");
      expect(ratingText).toBe("Rating: 8");
  });
});

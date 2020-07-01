import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorrySolutionSectionComponent } from './worry-solution-section.component';
import { By } from '@angular/platform-browser';
import { Solution } from '../solution';
import { MatButtonModule } from '@angular/material/button';

describe('WorrySolutionSectionComponent', () => {
  let component: WorrySolutionSectionComponent;
  let fixture: ComponentFixture<WorrySolutionSectionComponent>;
  let solutions: Solution[];

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WorrySolutionSectionComponent ],
      imports : [MatButtonModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorrySolutionSectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();

    solutions = [
      {description: "test1", advantages: "test1", disadvantages: "test1", timeLogged: new Date()},
      {description: "test2", advantages: "test2", disadvantages: "test2", timeLogged: new Date()},
      {description: "test3", advantages: "test3", disadvantages: "test3", timeLogged: new Date()},
      {description: "test4", advantages: "test4", disadvantages: "test4", timeLogged: new Date()},
    ]
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should display no solutions if dataset is empty', () => {
      let text = fixture.debugElement.query(By.css("h4")).nativeElement.textContent
      expect(text).toBe("No solutions logged for this worry");
  })
  it("should disable both buttons if list is empty", () => {
    let buttonLeftDisabled = fixture.debugElement.query(By.css("#leftButton")).nativeElement.disabled
    let buttonRightDisabled = fixture.debugElement.query(By.css("#rightButton")).nativeElement.disabled

    expect(buttonLeftDisabled).toBeTruthy();
    expect(buttonRightDisabled).toBeTruthy();
  })
  it("should disable only left button if at beginning of list (with data)", () => {
    component.solutions = solutions;
    fixture.detectChanges()
    let buttonLeftDisabled = fixture.debugElement.query(By.css("#leftButton")).nativeElement.disabled
    let buttonRightDisabled = fixture.debugElement.query(By.css("#rightButton")).nativeElement.disabled

    expect(buttonLeftDisabled).toBeTruthy();
    expect(buttonRightDisabled).toBeFalsy();
  });
  it("should disable only right button if at end of list (with data)", () => {
    component.solutions = solutions;
    component.currentIndex = solutions.length - 1;
    fixture.detectChanges()
    let buttonLeftDisabled = fixture.debugElement.query(By.css("#leftButton")).nativeElement.disabled
    let buttonRightDisabled = fixture.debugElement.query(By.css("#rightButton")).nativeElement.disabled

    expect(buttonLeftDisabled).toBeFalsy();
    expect(buttonRightDisabled).toBeTruthy();
  });
  it("should do nothing if onRightClicked is activated while at end of list", () => {
    component.solutions = solutions;
    component.currentIndex = solutions.length - 1;
    fixture.detectChanges()

    let originalIndex = component.currentIndex;
    component.onRightClicked();

    expect(component.currentIndex).toEqual(originalIndex);
  });
  it("should do nothing if onRightClicked is activated while at start of list", () => {
    component.solutions = solutions;
    component.currentIndex = 0;
    fixture.detectChanges()

    let originalIndex = component.currentIndex;
    component.onLeftClicked();

    expect(component.currentIndex).toEqual(originalIndex);
  });
  it("should display data correctly", () => {
    component.solutions = solutions;
    fixture.detectChanges();

    let descriptionText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent
    let advText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent
    let disadvText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent

    expect(descriptionText).toBe("test1")
    expect(advText).toBe("test1")
    expect(disadvText).toBe("test1")
  });
  it("should change displayed data if buttons are pressed", () => {
    component.solutions = solutions;
    fixture.detectChanges();

    let originalDescriptionText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent
    let originalAdvText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent
    let originalDisAdvText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent

    component.onRightClicked()
    fixture.detectChanges()

    let newDescriptionText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent
    let newAdvText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent
    let newDisAdvText = fixture.debugElement.query(By.css("#descriptionText")).nativeElement.textContent

    expect(newDescriptionText).not.toBe(originalDescriptionText);
    expect(newAdvText).not.toBe(originalAdvText);
    expect(newDisAdvText).not.toBe(originalAdvText)

    expect(newDescriptionText).toBe("test2")
    expect(newAdvText).toBe("test2")
    expect(newDisAdvText).toBe("test2")
   
  })
});

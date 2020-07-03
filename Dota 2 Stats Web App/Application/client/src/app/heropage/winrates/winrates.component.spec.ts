import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WinratesComponent } from './winrates.component';

describe('WinratesComponent', () => {
  let component: WinratesComponent;
  let fixture: ComponentFixture<WinratesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ WinratesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WinratesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

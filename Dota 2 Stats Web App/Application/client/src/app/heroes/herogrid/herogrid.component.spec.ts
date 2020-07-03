import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HerogridComponent } from './herogrid.component';

describe('HerogridComponent', () => {
  let component: HerogridComponent;
  let fixture: ComponentFixture<HerogridComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HerogridComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HerogridComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

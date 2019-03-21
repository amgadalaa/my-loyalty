import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LusersComponent } from './lusers.component';

describe('LusersComponent', () => {
  let component: LusersComponent;
  let fixture: ComponentFixture<LusersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LusersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LusersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

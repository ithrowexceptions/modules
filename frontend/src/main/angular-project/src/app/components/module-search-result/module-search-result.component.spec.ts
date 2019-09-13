import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ModuleSearchResultComponent } from './module-search-result.component';

describe('ModuleSearchResultComponent', () => {
  let component: ModuleSearchResultComponent;
  let fixture: ComponentFixture<ModuleSearchResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ModuleSearchResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ModuleSearchResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

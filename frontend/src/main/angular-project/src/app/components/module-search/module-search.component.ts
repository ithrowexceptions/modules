import { Component, OnInit, NgZone } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-module-search',
  templateUrl: './module-search.component.html',
  styleUrls: ['./module-search.component.css']
})

export class ModuleSearchComponent implements OnInit {
  moduleForm: FormGroup;
  ModuleArr: any = [];

  ngOnInit() {
    this.searchModule()
  }

  constructor(
    public fb: FormBuilder,
    private ngZone: NgZone,
    private router: Router,
    public moduleService: ModuleService
  ){}

  searchModule() {
    this.moduleForm = this.fb.group({
      name: undefined,
      surname: undefined,
      birthDate: undefined,
      creationTimestamp: undefined,
      age: undefined,
      type: undefined
    })
  }

  searchForm() {
    return this.moduleService.SearchModules(this.moduleForm.value).subscribe((res : any) => {
      this.ngZone.run(() => this.router.navigate(['/modules/search/result'], {queryParams: {name: JSON.stringify(res.content)}, skipLocationChange: true} ))
    });
  }

}

import { Component, OnInit, NgZone } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-add-module',
  templateUrl: './add-module.component.html',
  styleUrls: ['./add-module.component.css']
})

export class AddModuleComponent implements OnInit {
  moduleForm: FormGroup;
  ModuleArr: any = [];

  ngOnInit() {
    this.addModule()
  }

  constructor(
    public fb: FormBuilder,
    private ngZone: NgZone,
    private router: Router,
    public moduleService: ModuleService
  ){}

  addModule() {
    this.moduleForm = this.fb.group({
      name: [''],
      surname: [''],
      birthDate: [''],
      type: ['']
    })
  }

  submitForm() {
    this.moduleService.CreateModule(this.moduleForm.value).subscribe(res => {
      console.log('Module added!')
      this.ngZone.run(() => this.router.navigateByUrl('/modules'))
    });
  }

}

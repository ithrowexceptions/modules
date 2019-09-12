import { Component, OnInit, NgZone } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-edit-module',
  templateUrl: './edit-module.component.html',
  styleUrls: ['./edit-module.component.css']
})

export class EditModuleComponent implements OnInit {
  ModulesList: any = [];
  updateModuleForm: FormGroup;

  ngOnInit() {
    this.updateForm()
  }

  constructor(
    private actRoute: ActivatedRoute,
    public moduleService: ModuleService,
    public fb: FormBuilder,
    private ngZone: NgZone,
    private router: Router
  ) {
    var id = this.actRoute.snapshot.paramMap.get('id');
    this.moduleService.GetModuleById(id).subscribe((data) => {
      this.updateModuleForm = this.fb.group({
        id: [data.id],
        name: [data.name],
        surname: [data.surname],
        birthDate: [data.birthDate],
        creationTimestamp: [data.creationTimestamp],
        age: [data.age],
        type: [data.type]
      })
    })
  }

  updateForm(){
    this.updateModuleForm = this.fb.group({
      id: [''],
      name: [''],
      surname: [''],
      birthDate: [''],
      creationTimestamp: [''],
      age: [''],
      type: ['']
    })
  }

  submitForm(){
    var id = this.actRoute.snapshot.paramMap.get('id');
    this.moduleService.UpdateModule(id, this.updateModuleForm.value).subscribe(res => {
      this.ngZone.run(() => this.router.navigateByUrl('/modules'))
    })
  }

}

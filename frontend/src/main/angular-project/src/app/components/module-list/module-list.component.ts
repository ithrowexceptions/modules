import { Component, OnInit } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { ActivatedRoute } from "@angular/router";
import { Router } from '@angular/router';

@Component({
  selector: 'app-module-list',
  templateUrl: './module-list.component.html',
  styleUrls: ['./module-list.component.css']
})
export class ModuleListComponent implements OnInit {

  ModulesList: any = [];
  config: any;

  ngOnInit() {
    this.loadModules();
  }
  constructor(
    public moduleService: ModuleService,
    private route: ActivatedRoute,
    private router: Router
  ){}

   // Get modules
   loadModules() {
    var urlParameter = null;

    if(this.route.snapshot.paramMap.get("id") !== null){
      urlParameter = this.route.snapshot.paramMap.get("id");
      return this.moduleService.GetModuleById(urlParameter).subscribe((data: {}) => {
        this.ModulesList = [data];
      })
    }

    if(this.route.snapshot.paramMap.get("name") !== null){
      urlParameter = this.route.snapshot.paramMap.get("name");
      return this.moduleService.GetModulesByName(urlParameter).subscribe((data: {}) => {
        this.ModulesList = data;
      })
    }

    if(this.route.snapshot.paramMap.get("surname") !== null){
      urlParameter = this.route.snapshot.paramMap.get("surname");
      return this.moduleService.GetModulesBySurname(urlParameter).subscribe((data: {}) => {
        this.ModulesList = data;
      })
    }

    if(this.route.snapshot.paramMap.get("birthdate") !== null){
      urlParameter = this.route.snapshot.paramMap.get("birthdate");
      return this.moduleService.GetModulesByBirthdate(urlParameter).subscribe((data: {}) => {
        this.ModulesList = data;
      })
    }

    if(this.route.snapshot.paramMap.get("creationTimestamp") !== null){
      urlParameter = this.route.snapshot.paramMap.get("creationTimestamp");
      return this.moduleService.GetModulesByTimestamp(urlParameter).subscribe((data: {}) => {
        this.ModulesList = data;
      })
    }

    if(this.route.snapshot.paramMap.get("age") !== null){
      urlParameter = this.route.snapshot.paramMap.get("age");
      return this.moduleService.GetModulesByAge(urlParameter).subscribe((data: {}) => {
        this.ModulesList = data;
      })
    }

    if(this.route.snapshot.paramMap.get("type") !== null){
      urlParameter = this.route.snapshot.paramMap.get("type");
      return this.moduleService.GetModulesByType(urlParameter).subscribe((data: {}) => {
        this.ModulesList = data;
      })
    }

    return this.moduleService.GetModules().subscribe((data: {}) => {
        this.ModulesList = data;
    })
  }

   // Delete module
   deleteModule(data){
     var index = index = this.ModulesList.map(x => {return x.name}).indexOf(data.surname);
      return this.moduleService.DeleteModule(data.id).subscribe(res => {
        this.ModulesList.splice(index, 1)
        console.log('Module deleted!')
     })
   }

   // Get Module Report
   downloadPDF(id){
       return this.moduleService.GetReportOfModulesById(id).subscribe( (data : any) => {
         var url= window.URL.createObjectURL(data);
         window.open(url);
     })
   }

}

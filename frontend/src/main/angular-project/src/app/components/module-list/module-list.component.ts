import { Component, OnInit } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'app-module-list',
  templateUrl: './module-list.component.html',
  styleUrls: ['./module-list.component.css']
})
export class ModuleListComponent implements OnInit {

  ModulesList: any = [];

  ngOnInit() {
    this.loadModules();
  }
  constructor(
    public moduleService: ModuleService,
    private route: ActivatedRoute
  ){}

   // Modules list
   loadModules() {
    var urlParameter = null;
    if(this.route.snapshot.paramMap.get("name") !== null){
      urlParameter = this.route.snapshot.paramMap.get("name");
      console.log(urlParameter);
      return this.moduleService.GetModules().subscribe((data: {}) => {
        for(var x = 0; x < Object.keys(data).length; x++){
          console.log(data[x].name.toLowerCase());
          console.log(urlParameter);
          if( data[x].name.toLowerCase() == urlParameter){
            console.log(data);
            console.log(data[x].name);
            this.ModulesList.push(data[x]);
          }
        }
      })
      console.log(urlParameter);
    }

    if(this.route.snapshot.paramMap.get("surname") !== null){
      urlParameter = this.route.snapshot.paramMap.get("surname");
      return this.moduleService.GetModules().subscribe((data: {}) => {
        if(urlParameter === null){
          this.ModulesList = data;
        } else {
          for(var x = 0; x < Object.keys(data).length; x++){
            if( data[x].surname == urlParameter){
              this.ModulesList.push(data[x]);
            }
          }
        }
      })
      console.log(urlParameter);
    }

    console.log(urlParameter)
    return this.moduleService.GetModules().subscribe((data: {}) => {
        this.ModulesList = data;
        console.log(data);
    })
  }

    // Delete module
    deleteIusse(data){
      var index = index = this.ModulesList.map(x => {return x.name}).indexOf(data.surname);
       return this.moduleService.DeleteModule(data.id).subscribe(res => {
        this.ModulesList.splice(index, 1)
         console.log('Module deleted!')
       })
    }

}

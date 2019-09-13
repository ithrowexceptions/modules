import { Component, OnInit } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'app-module-search-result',
  templateUrl: './module-search-result.component.html',
  styleUrls: ['./module-search-result.component.css']
})
export class ModuleSearchResultComponent implements OnInit {

  ModulesList: any = [];

  ngOnInit() {
    this.loadPage();
  }

  constructor(
    public moduleService: ModuleService,
    private route: ActivatedRoute,
  ){}

   // load page
   loadPage(){
       this.route.queryParams.subscribe((params) => {
           this.ModulesList = JSON.parse(params.name);
       });

   }

   // Delete module
   deleteModule(data){
     var index = index = this.ModulesList.map(x => {return x.name}).indexOf(data.surname);
      return this.moduleService.DeleteModule(data.id).subscribe(res => {
        this.ModulesList.splice(index, 1)
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

import { Component, OnInit } from '@angular/core';
import { ModuleService } from '../../services/module.service';
import { ActivatedRoute } from "@angular/router";

@Component({
  selector: 'app-module-page',
  templateUrl: './module-page.component.html',
  styleUrls: ['./module-page.component.css']
})
export class ModulePageComponent implements OnInit {

  ModulesList: any = [];
  config : { itemsPerPage : any ,
             currentPage : any,
             totalItems: any
  };
  isThisFirstPage : any;
  isThisLastPage : any;

  ngOnInit() {
    this.loadPage();
  }

  constructor(
    public moduleService: ModuleService,
    private route: ActivatedRoute,
  ){}

   // load page
   loadPage(){
     this.config = {itemsPerPage : 5 ,
               currentPage : 0,
               totalItems : 0
     };
     this.isThisFirstPage = true;
     this.isThisLastPage = false;
     return this.moduleService.GetPage(undefined,undefined).subscribe((data: any) => {
      this.config.itemsPerPage = data.numberOfElements;
      this.config.currentPage = data.pageable.pageNumber;
      this.config.totalItems = data.content.size;
      this.ModulesList = data.content;
      this.isThisFirstPage = data.first;
      this.isThisLastPage = data.last;
     });
   }

   previousPage(){
    return this.moduleService.GetPage(this.config.currentPage-1,this.config.itemsPerPage).subscribe((data: any) => {
          this.config.itemsPerPage = data.numberOfElements;
          this.config.currentPage = data.pageable.pageNumber;
          this.config.totalItems = data.content.size;
          this.ModulesList = data.content;
          this.isThisFirstPage = data.first;
          this.isThisLastPage = data.last;
         });
   }


   nextPage(){
    return this.moduleService.GetPage(this.config.currentPage+1,this.config.itemsPerPage).subscribe((data: any) => {
          this.config.itemsPerPage = data.numberOfElements;
          this.config.currentPage = data.pageable.pageNumber;
          this.config.totalItems = data.content.size;
          this.ModulesList = data.content;
          this.isThisFirstPage = data.first;
          this.isThisLastPage = data.last;
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

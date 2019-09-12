import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { NgxPaginationModule } from 'ngx-pagination';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

/* Http client module */
import { HttpClientModule } from '@angular/common/http';

/* Service */
import { ModuleService } from './services/module.service';

/* Forms module */
import { ReactiveFormsModule } from '@angular/forms';

/* Components */
import { AddModuleComponent } from './components/add-module/add-module.component';
import { EditModuleComponent } from './components/edit-module/edit-module.component';
import { ModuleListComponent } from './components/module-list/module-list.component';
import { IndexComponent } from './components/index/index.component';
import { ModulePageComponent } from './components/module-page/module-page.component';


@NgModule({
  declarations: [
    AppComponent,
    AddModuleComponent,
    EditModuleComponent,
    ModuleListComponent,
    IndexComponent,
    ModulePageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    NgxPaginationModule
  ],
  providers: [ModuleService],
  bootstrap: [AppComponent]
})

export class AppModule { }

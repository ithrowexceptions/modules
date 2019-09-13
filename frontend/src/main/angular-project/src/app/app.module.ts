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
import { ModuleAddComponent } from './components/module-add/module-add.component';
import { ModuleEditComponent } from './components/module-edit/module-edit.component';
import { ModuleListComponent } from './components/module-list/module-list.component';
import { IndexComponent } from './components/index/index.component';
import { ModulePageComponent } from './components/module-page/module-page.component';
import { ModuleSearchComponent } from './components/module-search/module-search.component';
import { ModuleSearchResultComponent } from './components/module-search-result/module-search-result.component';


@NgModule({
  declarations: [
    AppComponent,
    ModuleAddComponent,
    ModuleEditComponent,
    ModuleListComponent,
    IndexComponent,
    ModulePageComponent,
    ModuleSearchComponent,
    ModuleSearchResultComponent
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

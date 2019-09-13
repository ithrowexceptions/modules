import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ModuleAddComponent } from './components/module-add/module-add.component';
import { ModuleEditComponent } from './components/module-edit/module-edit.component';
import { ModuleListComponent } from './components/module-list/module-list.component';
import { IndexComponent } from './components/index/index.component';
import { ModulePageComponent } from './components/module-page/module-page.component';
import { ModuleSearchComponent } from './components/module-search/module-search.component';

const routes: Routes = [
  { path: '', component: IndexComponent },
  { path: '', pathMatch: 'full', redirectTo: 'modules' },
  { path: 'modules/page', component: ModulePageComponent },
  { path: 'modules/search', component: ModuleSearchComponent },
  { path: 'module-add', component: ModuleAddComponent },
  { path: 'module-edit/:id', component: ModuleEditComponent },
  { path: 'modules', component: ModuleListComponent },
  { path: 'modules/:id', component: ModuleListComponent },
  { path: 'modules/name/:name', component: ModuleListComponent },
  { path: 'modules/surname/:surname', component: ModuleListComponent },
  { path: 'modules/birthdate/:birthdate', component: ModuleListComponent },
  { path: 'modules/timestamp/:creationTimestamp', component: ModuleListComponent },
  { path: 'modules/age/:age', component: ModuleListComponent },
  { path: 'modules/type/:type', component: ModuleListComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})

export class AppRoutingModule { }

import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AddModuleComponent } from './components/add-module/add-module.component';
import { EditModuleComponent } from './components/edit-module/edit-module.component';
import { ModuleListComponent } from './components/module-list/module-list.component';
import { IndexComponent } from './components/index/index.component';
import { ModulePageComponent } from './components/module-page/module-page.component';

const routes: Routes = [
  { path: '', component: IndexComponent },
  { path: '', pathMatch: 'full', redirectTo: 'modules' },
  { path: 'modules/page', component: ModulePageComponent },
  { path: 'add-module', component: AddModuleComponent },
  { path: 'edit-module/:id', component: EditModuleComponent },
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

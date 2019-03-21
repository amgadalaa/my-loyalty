import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LusersComponent } from './lusers/lusers.component';

const routes: Routes = [{
  path: '',
  children: [
    {
      path: '',
      redirectTo: 'lusers'
    },
    {
      path: 'lusers',
      component: LusersComponent
    }
  ]
}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class UsersRoutingModule { }

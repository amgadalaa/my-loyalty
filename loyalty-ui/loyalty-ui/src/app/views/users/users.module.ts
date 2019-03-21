import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {  MatTableModule } from "@angular/material";

import { UsersRoutingModule } from './users-routing.module';
import { LusersComponent } from './lusers/lusers.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule }   from '@angular/forms';


@NgModule({
  declarations: [LusersComponent],
  imports: [
    CommonModule,
    UsersRoutingModule,
    MatTableModule,
    HttpClientModule,
    FormsModule,
  ]
})
export class UsersModule { }

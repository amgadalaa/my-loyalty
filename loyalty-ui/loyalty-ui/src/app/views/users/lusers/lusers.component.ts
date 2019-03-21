import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { LusersApiService } from './dataservice/lusers.ApiService';
import { LusersModel } from './models/lusers.models';
import { Subscription } from 'rxjs';
import {MatTable} from '@angular/material';

@Component({
  selector: 'app-lusers',
  templateUrl: './lusers.component.html',
  styleUrls: ['./lusers.component.scss']
})
export class LusersComponent implements OnInit, OnDestroy {

  displayedColumns: string[] = ['id', 'name', 'edit'];
  allUsers: LusersModel[];
  subscription: Subscription;
  addSubscription: Subscription;
  isEdit: boolean;
  isAddOrEdit: boolean;
  @ViewChild('lusers-list-table') table: MatTable<any>;

  tempLusr: LusersModel;
  constructor(private luserAPIServ: LusersApiService) {
  }

  ngOnInit() {
    this.subscription = this.luserAPIServ.getAllLUsers().subscribe(
      res => {
        console.log("test:" + res);
        this.allUsers = res;
        //        this.loading = false;
      },
      err => {
        console.error(err);
      }
    );

    this.isAddOrEdit = false;

    this.tempLusr = new LusersModel();
  }

  onEditClick(luserId: number): void {
    console.log('received id:' + luserId);
    this.isAddOrEdit = true;
    this.isEdit = true;
    this.tempLusr = this.allUsers.find(lusr => lusr.id === luserId);

  }


  onAddEditSubmit(): void {
    //TODO backend validation
    if (this.isEdit) {
      // update server

      //update existing list
      this.allUsers.splice(this.allUsers.findIndex(lusr => lusr.id === this.tempLusr.id), 1, this.tempLusr);

    }
    else {
      //update server
      this.addSubscription = this.luserAPIServ.addNewLusr(this.tempLusr).subscribe(res => {
        console.log("test:" + res);
        this.tempLusr = res;
        this.allUsers.push(this.tempLusr);
        //        this.loading = false;
      },
        err => {
          console.error(err);
        }
      );

    }
    this.activateViewMode();
    this.table.renderRows();
  }


  activateAddMode(): void {
    console.log("activateAddMode");
    this.isAddOrEdit = true;
    this.isEdit = false;
    //reset 
    this.tempLusr = new LusersModel();
  }


  private activateViewMode(): void {
    this.isAddOrEdit = false;
    this.isEdit = false;
  }


  ngOnDestroy(): void {
    this.subscription.unsubscribe();

    this.addSubscription && this.addSubscription.unsubscribe();
  }

}


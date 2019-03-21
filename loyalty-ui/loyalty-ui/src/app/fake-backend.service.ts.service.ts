import { Injectable } from '@angular/core';
import {InMemoryDbService} from 'angular-in-memory-web-api';
import { LusersModel } from './views/users/lusers/models/lusers.models';



@Injectable({
  providedIn: 'root'
})
export class FakeBackendService implements InMemoryDbService{
  
  
  
  
    createDb() {
       let lusrs : LusersModel[] = [
       {
         id: 1,         
         username: 'Amgad',
         email: 'e1',
         fullName: 'e1',         
         isEnabled: false,
         password: 'pass'
       },
       {
        id: 2,        
         username: 'Amgad2',
         email: 'e2',
         fullName: 'e2',         
         isEnabled: false,
         password: 'pass2'
       },
       {
        id: 3,        
         username: 'Amgad3',
         email: 'e3',
         fullName: 'e3',         
         isEnabled: false,
         password: 'pass3'
       }
      ];
      return {
        'lusrs': lusrs
      };
  
  }

  constructor() { }



  genId(lusers: LusersModel[]): number {
    return lusers.length > 0 ? Math.max(...lusers.map(user => user.id)) + 1 : 11;
}


}

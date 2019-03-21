import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { throwError as ObservableThrowError, Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { LusersModel } from '../models/lusers.models';
import { environment } from './../../../../../environments/environment';

@Injectable({
    providedIn: 'root'
}
)
export class LusersApiService {

    basePath: string;
    constructor(
        private http: HttpClient
    ) {

        this.basePath = `${environment.USER_MGNT_URL}/lusrs`;

    }


    getAllLUsers(): Observable<LusersModel[]> {
        return this.http.get<LusersModel[]>(this.basePath).pipe(
            catchError((error) => this._handleError(error))
        );
    }

    addNewLusr(toInsert: LusersModel): Observable<LusersModel> {

        return this.http.post<LusersModel>(this.basePath, toInsert).pipe(
            catchError((error) => this._handleError(error))
        );
    }


    private _handleError(err: HttpErrorResponse | any): Observable<any> {
        console.log(`Error: ${err.status} and body: ${err.body.error} `)
        const errorMsg = err.message || 'Error: Unable to complete request.';
        return ObservableThrowError(errorMsg);
    }
}
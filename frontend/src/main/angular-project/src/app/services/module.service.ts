import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Module } from './module';
import { Page } from './page';
import { Observable, throwError } from 'rxjs';
import { retry, catchError } from 'rxjs/operators';
import { Component, OnInit } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class ModuleService {

  // Base url
  baseurl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  // Http Headers
  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  }

  // GET
  GetModules(): Observable<Module> {
    return this.http.get<Module>(this.baseurl + '/modules')
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  //GET
  GetModuleById(id): Observable<Module> {
    return this.http.get<Module>(this.baseurl + '/modules/' + id)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  //GET
  GetModulesByName(name): Observable<Module> {
    return this.http.get<Module>(this.baseurl + '/modules/name/' + name)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  //GET
  GetModulesBySurname(surname): Observable<Module> {
    return this.http.get<Module>(this.baseurl + '/modules/surname/' + surname)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  //GET
  GetModulesByBirthdate(birthdate): Observable<Module> {
    return this.http.get<Module>(this.baseurl + '/modules/birthdate/' + birthdate)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  //GET
  GetModulesByTimestamp(creationTimeStamp): Observable<Module> {
    return this.http.get<Module>(this.baseurl + '/modules/timestamp/' + creationTimeStamp)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  //GET
  GetModulesByAge(age): Observable<Module> {
    return this.http.get<Module>(this.baseurl + '/modules/age/' + age)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  //GET
  GetModulesByType(type): Observable<Module> {
    return this.http.get<Module>(this.baseurl + '/modules/type/' + type)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  GetPage(page?: number, size?: number): Observable<Page>{
    if(!page && !size){
      return this.http.get<Page>(this.baseurl + '/modules/page')
        .pipe(
         retry(1),
         catchError(this.errorHandl)
       )
    } else if(!page && size) {
      return this.http.get<Page>(this.baseurl + '/modules/page?size=' + size)
        .pipe(
          retry(1),
          catchError(this.errorHandl)
        )
    } else if(page && !size){
      return this.http.get<Page>(this.baseurl + '/modules/page?page=' + page)
        .pipe(
          retry(1),
          catchError(this.errorHandl)
        )
    } else {
      return this.http.get<Page>(this.baseurl + '/modules/page?page=' + page +"&size=" + size)
        .pipe(
          retry(1),
          catchError(this.errorHandl)
        )
    }
  }

  //GET
  GetReportOfModulesById(id): any {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf');
    return this.http.get(this.baseurl + '/modules/report/' + id, { headers: headers, responseType: 'blob' })
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  //GET
  SearchModules(data): Observable<Module>{
    var inputUrl = this.baseurl + "/modules/search?";
    console.log(data)
    if(data.name){
      inputUrl = inputUrl + "name="+data.name+"&";
    }

    if(data.surname){
      inputUrl = inputUrl + "surname="+data.surname+"&";
    }

    if(data.birthdate){
      inputUrl = inputUrl + "birthdate="+data.birthdate+"&";
    }

    if(data.timestamp){
      inputUrl = inputUrl + "timestamp="+data.timestamp+"&";
    }

    if(data.age){
      inputUrl = inputUrl + "age="+data.age+"&";
    }

    if(data.type){
      inputUrl = inputUrl + "type="+data.type;
    }

    return this.http.get<Module>(inputUrl)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )

  }

/*
  //GET
  SearchModules(name?: String, surname?: String, birthdate?: String, timestamp?: String, age?: number, type?: String): Observable<Module>{
    var inputUrl = this.baseurl + "/modules/search?";

    if(name){
      inputUrl = inputUrl + "name="+name+"&";
    }

    if(surname){
      inputUrl = inputUrl + "surname="+surname+"&";
    }

    if(birthdate){
      inputUrl = inputUrl + "birthdate="+birthdate+"&";
    }

    if(timestamp){
      inputUrl = inputUrl + "timestamp="+timestamp+"&";
    }

    if(age){
      inputUrl = inputUrl + "age="+age+"&";
    }

    if(type){
      inputUrl = inputUrl + "type="+type;
    }

    return this.http.get<Module>(inputUrl)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )

  }
*/
  // POST
  CreateModule(data): Observable<Module> {
    return this.http.post<Module>(this.baseurl + '/modules', JSON.stringify(data), this.httpOptions)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  // PUT
  UpdateModule(id, data): Observable<Module> {
    return this.http.put<Module>(this.baseurl + '/modules', JSON.stringify(data), this.httpOptions)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  // DELETE
  DeleteModule(id){
    return this.http.delete<Module>(this.baseurl + '/modules/' + id, this.httpOptions)
    .pipe(
      retry(1),
      catchError(this.errorHandl)
    )
  }

  // Error handling
  errorHandl(error) {
     let errorMessage = '';
     if(error.error instanceof ErrorEvent) {
       // Get client-side error
       errorMessage = error.error.message;
     } else {
       // Get server-side error
       errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
     }
     console.log(errorMessage);
     return throwError(errorMessage);
  }
}

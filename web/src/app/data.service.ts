import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map, catchError, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class DataService {

  constructor(private http: HttpClient) {}

  private handleError(error: any): Promise<any> {
    console.error('An error occurred', error); // for demo purposes only
    return Promise.reject(error.message || error);
  }

  helloWorld (): Observable<string> {
    const headers = new HttpHeaders({ 'Content-Type': 'text/plain'});
    return this.http.get('v1/hello', {responseType: 'text', headers});
  }

  getLogin (username, password): Observable<any> {
    return this.http.get('v1/login', {headers: new HttpHeaders({'username': username, 'password': password})})
      .pipe(catchError(this.handleError));
  }

}

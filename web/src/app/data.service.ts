import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class DataService {

  constructor(private http: HttpClient) { }

  getLogin(username, password){
    return this.http.get('http://216.171.5.211:61235/api/v1/login', {headers: new HttpHeaders({'username': username, 'password': password})});
  }

  getFriends() {
    return this.http.get('http://216.171.5.211:61235/api/v1/friends');
  }

}

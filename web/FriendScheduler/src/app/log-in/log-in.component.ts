import { Component, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { DataService } from "../data.service";


@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})

export class LogInComponent implements OnInit {

  login$: Object;

  constructor(private router: Router, private data: DataService) {}

  onSubmit(){
    this.router.navigate(['/home']);
    // this.data.getLogin(username, password).subscribe(
    //   data => this.login$ = data
    // );
    // console.log(this.login$.toString());
  }

  ngOnInit() {
  }

}

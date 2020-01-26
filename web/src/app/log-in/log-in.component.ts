import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';


@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})

export class LogInComponent implements OnInit {

  login$;

  constructor(private router: Router, private data: DataService) {}

  onSubmit(username, password) {
    this.data.getLogin(username, password).subscribe(data => console.log(data.headers.get('Token')));
    // if (token > 0) {
    this.router.navigate(['/home']);
    // } else {
    //   console.log('You fucked up');
    // }
  }

  ngOnInit() {
  }

}

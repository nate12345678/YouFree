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
    this.data.getLogin(username, password).subscribe(data => this.login$ = data);
    // if (this.login$.status === 200) {
    this.router.navigate(['/home']);
    // }
  }

  ngOnInit() {
  }

}

import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material';

export interface Friends {
  name: string;
  isAvail: string;
  nextAvail: string;
}

@Component({
  selector: 'app-friends',
  templateUrl: './friends.component.html',
  styleUrls: ['./friends.component.css']
})
export class FriendsComponent implements OnInit {

  friends: Friends[] = [];
  sortedData: Friends[];

  constructor() { }

  ngOnInit() {
  }

  sortData(sort: Sort) {
    const data = this.friends.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name': return compare(a.name, b.name, isAsc);
        case 'isAvail': return compare(a.isAvail, b.isAvail, isAsc);
        case 'nextAvail': return compare(a.nextAvail, b.nextAvail, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material';
import { DataService } from '../data.service';


export interface AvailFriends {
  name: string;
  availTime: number;
}

export interface Tiles {
  id: number;
  cols: number;
  rows: number;
  color: string;
  time: string;

}


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  avail: AvailFriends[] = [
    // {name: 'Nate Post', availTime: 200, },
    // {name: 'Patrick Ubelhor', availTime: 100},
  ];

  sortedData: AvailFriends[];
  tiles: Tiles[] = [];

  constructor(private data: DataService) {
    this.sortedData = this.avail.slice();
  }

  createGrid() {
    let num = 0;
    let hour = 0;
    let minutes = 0;

    for (let i = 0; i < 96; i++) {
      for (let j = 0; j < 8; j++) {
        if (j === 0) {
          this.tiles[num] = {id: num, cols: 1, rows: 1, color: '', time: hour.toString() + ':' + minutes.toString()};
        } else {
          this.tiles[num] = {id: num, cols: 1, rows: 1, color: '#37da0d', time: ''};
        }

        num++;
      }

      if (minutes === 45) {
        hour++;
        minutes = 0;
      } else {
        minutes += 15;
      }

    }

  }

  ngOnInit() {
  }


  sortData(sort: Sort) {
    const data = this.avail.slice();
    if (!sort.active || sort.direction === '') {
      this.sortedData = data;
      return;
    }

    this.sortedData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name': return compare(a.name, b.name, isAsc);
        case 'availTime': return compare(a.availTime, b.availTime, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

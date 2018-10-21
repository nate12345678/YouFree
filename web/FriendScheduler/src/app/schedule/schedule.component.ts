import { Component, OnInit } from '@angular/core';

export interface Tiles {
  id: number;
  cols: number;
  rows: number;
  color: string;
  isOn: boolean;
  time: string;

}

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css']
})
export class ScheduleComponent implements OnInit {

  tiles:Tiles[] = [];

  constructor() {
    this.createGrid();
  }

  changeColor(id){
      // if(this.tiles[id].color === '#37da0d'){
      //   this.tiles[id].color = '#d82200';
      // } else {
      //   this.tiles[id].color = '#37da0d';
      // }
      return this.tiles[id].color;
    }

  createGrid(){
    var num = 0;
    var hour = 0;
    var minutes = 0;

    for(var i = 0; i < 96; i++){
      for(var j = 0; j < 8; j++){
        if(j == 0){
          this.tiles[num] = {id: num, cols: 1, rows: 1, color: '',isOn: false, time: hour.toString() + ':' + minutes.toString()};
        } else {
          this.tiles[num] = {id: num, cols: 1, rows: 1, color: '#37da0d',isOn: true, time:""};
        }

        num++;
      }

      if(minutes == 45){
        hour++;
        minutes = 0;
      } else {
        minutes += 15;
      }

    }

  }


  ngOnInit() {
  }

}

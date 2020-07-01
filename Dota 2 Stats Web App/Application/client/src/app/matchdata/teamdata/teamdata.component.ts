import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-teamdata',
  templateUrl: './teamdata.component.html',
  styleUrls: ['./teamdata.component.css']
})
export class TeamdataComponent implements OnInit {

  @Input() matchdata;
  @Input() teamdata;
  
  constructor() { }

  ngOnInit() {

  }

}

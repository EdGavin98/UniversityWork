import { Component, OnInit, Input } from '@angular/core';
import { MatchService } from 'src/app/match.service';

@Component({
  selector: 'app-scoreboard',
  templateUrl: './scoreboard.component.html',
  styleUrls: ['./scoreboard.component.css']
})
export class ScoreboardComponent implements OnInit {
  @Input() matchdata;

  constructor() { }

  ngOnInit() {
  }

  whoWon() : string {
    if (this.matchdata.radiantVictory)  {
      return this.matchdata.radiant.name + " Victory";
    } else {
      return this.matchdata.dire.name + " Victory";
    }
  }

  

}

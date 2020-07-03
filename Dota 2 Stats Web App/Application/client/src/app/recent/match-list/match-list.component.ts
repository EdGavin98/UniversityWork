import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatchService } from 'src/app/match.service';
import { Router } from '@angular/router';
import * as io from 'socket.io-client';

@Component({
  selector: 'app-match-list',
  templateUrl: './match-list.component.html',
  styleUrls: ['./match-list.component.css']
})
export class MatchListComponent implements OnInit {

  constructor(private service : MatchService,
              private router: Router) { }
  data : {}
  views : {}
  socket : any;

  ngOnInit() {
    this.getRecent();
    this.getCounts();
    this.socket = io("localhost:8080/recent-namespace");

    
    this.socket.on('match-viewed', () => {
      this.getRecent();
      this.getCounts();
    });
  }

  ngOnDestroy() {
    this.socket.disconnect();
  }

  formattedDate(date) {
    var tempDate = new Date(date);
    var mins = tempDate.getMinutes() < 10 ? "0" + tempDate.getMinutes() : tempDate.getMinutes();
    return tempDate.getUTCDate() + "/" + (tempDate.getUTCMonth() + 1) + "/" + tempDate.getFullYear()
     + " at " + tempDate.getHours() + ":" + mins;
  }

  search(id) {
    this.router.navigate(['matchDetails/', id]);
  }

  getRecent() {
    this.service.getRecentlyViewedMatches().subscribe((obj : Response) => {
      this.data = obj;
    });
  }

  getCounts() {
    this.service.getViewCounts().subscribe((obj : Response) => {
      this.views = obj;
    });
  }

}

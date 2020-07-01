import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  constructor(private router: Router) { }


  matchId : string;
  invalid = false;

  
  ngOnInit() {
    
  }

  searchForMatch() {
    if (this.matchId != null || this.matchId || undefined)
      this.router.navigate(['matchDetails/', this.matchId]);
    else
      this.invalid = true;
  }

}

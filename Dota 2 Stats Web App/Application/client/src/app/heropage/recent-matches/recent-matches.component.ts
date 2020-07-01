import { Component, OnInit } from '@angular/core';
import { MatchService } from 'src/app/match.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-recent-matches',
  templateUrl: './recent-matches.component.html',
  styleUrls: ['./recent-matches.component.css']
})
export class RecentMatchesComponent implements OnInit {

  constructor(private service: MatchService, private route: ActivatedRoute, private router : Router) { }

  id : String;
  recentMatches : {};

  ngOnInit() {
    var paramMap = this.route.snapshot.paramMap;
    this.id = paramMap.get('id');

    this.service.getHeroRecents(this.id).subscribe((obj : Response) => {
      this.recentMatches = obj;
    });
  }

  search(matchId : String) {
    this.router.navigate(["matchDetails/", matchId]);
  }

}

import { Component, OnInit } from '@angular/core';
import { MatchService } from 'src/app/match.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-heropage',
  templateUrl: './heropage.component.html',
  styleUrls: ['./heropage.component.css']
})
export class HeropageComponent implements OnInit {

  constructor(private service: MatchService, private route : ActivatedRoute) { }

  heroId : String;
  matchups : any = {};
  heroData : any = {};
  winrates : any = {};
  loading = true;
  winrateLoading = true;
  matchupLoading = true;

  ngOnInit() {
    var paramMap = this.route.snapshot.paramMap;
    this.heroId = paramMap.get('id');

    this.service.getMatchupData(this.heroId).subscribe((obj: Response) => {
      this.matchups = obj;
      this.matchupLoading = false;

    });

    this.service.getHeroData(this.heroId).subscribe((obj : Response) => {
      this.heroData = obj;
      this.loading = false;

    });

    this.service.getHeroWinrates(this.heroId).subscribe((obj : Response) => {
      this.winrates = obj;
      this.winrateLoading = false;
    });


  }
}

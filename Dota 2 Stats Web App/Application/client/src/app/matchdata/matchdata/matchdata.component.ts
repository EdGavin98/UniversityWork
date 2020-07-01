import { Component, OnInit } from '@angular/core';
import { MatchService } from 'src/app/match.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-matchdata',
  templateUrl: './matchdata.component.html',
  styleUrls: ['./matchdata.component.css']
})
export class MatchdataComponent implements OnInit {

  constructor(private service : MatchService, private route: ActivatedRoute, private router : Router) { }

  data : any = {};
  loading = true;
  id : String;

  ngOnInit() {
    var paramMap = this.route.snapshot.paramMap;
    this.id = paramMap.get('id');

    this.service.searchForMatch(this.id).subscribe( 
      (obj) => {
        this.data = obj.body;
        this.loading = false;
      }, 
      (error) => {
        alert("Match does not exist, returning to home page");
        this.router.navigate(['/']);
      });
  }
}

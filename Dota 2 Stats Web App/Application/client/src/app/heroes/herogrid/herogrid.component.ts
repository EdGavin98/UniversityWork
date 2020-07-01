import { Component, OnInit } from '@angular/core';
import { MatchService } from 'src/app/match.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-herogrid',
  templateUrl: './herogrid.component.html',
  styleUrls: ['./herogrid.component.css']
})
export class HerogridComponent implements OnInit {

  constructor(private service : MatchService, private router : Router) { }

  heroes : any;
  loaded = false;
  hoveredElement : Number;
  displayHeroes : any;
  selectedSort = "default";

  ngOnInit() {
    this.getHeroes();
  }

  getHeroes() {
    this.service.getHeroList().subscribe((obj : Response) => {
      this.heroes = obj;
      this.displayHeroes = this.heroes.slice(0, this.heroes.length);
      this.loaded = true;
    });
  }

  viewDetails(id : String) {
    this.router.navigate(['heroes/', id])
  }

  getColour(attribute) {
    switch(attribute) {
      case "int":
        return {'background-color' : "rgba(0, 0, 255, 0.1)"};
      case "str":
        return {'background-color' : "rgba(255, 0, 0, 0.1)"};
      case "agi":
        return {'background-color' : "rgba(0, 255, 0, 0.1)"};
      default: 
        return  {'background-color' : "rgba(100, 100, 100, 0.1)"};
    }
  }

  onChange() {
    if (this.selectedSort == "default") {

      this.displayHeroes = this.heroes.slice(0, this.heroes.length);

    } else if (this.selectedSort == "alphabetical") {

      this.displayHeroes = this.displayHeroes.sort((a, b) => {
        if (a.name > b.name) {
          return 1;
        } else {
          return -1;
        }
      });

    } else if (this.selectedSort == "attribute") {

      this.displayHeroes = this.displayHeroes.sort((a, b) => {
        if (a.attribute > b.attribute) {
          return 1;
        } else {
          return -1;
        }
      });

    }
  }

}

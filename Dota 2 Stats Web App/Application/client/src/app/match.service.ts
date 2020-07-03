import { Injectable, Input } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  constructor(private http: HttpClient) { }

  url = "http://localhost:8080/api/"
  
  searchForMatch(matchId : String) {
    return this.http.get(this.url + "match/" + matchId, {observe : 'response'});
  }

  getRecentlyViewedMatches() {
    return this.http.get(this.url + "recentlyViewed");
  }

  getViewCounts() {
    return this.http.get(this.url + "recentCount");
  }

  getMatchComments(matchId : String) {
    return this.http.get(this.url + "match/" + matchId + "/comments");
  }

  postComment(matchId : String, comment : {}) {
    return this.http.post(this.url + "match/" + matchId + "/addComment", comment);
  }

  getHeroList() {
    return this.http.get(this.url + "allheroes");
  }

  getMatchupData(heroId : String) {
    return this.http.get(this.url + heroId + "/matchups");
  }

  getHeroRecents(heroId: String) {
    return this.http.get(this.url + heroId + "/matches");
  }

  getHeroData(heroId: String) {
    return this.http.get(this.url + "hero/" + heroId);
  }

  getHeroWinrates(heroId: String) {
    return this.http.get(this.url + heroId + "/winrates");
  }

}

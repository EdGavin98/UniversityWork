import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SearchComponent } from './home/search/search.component';
import { MatchdataComponent} from './matchdata/matchdata/matchdata.component';
import { MatchListComponent } from './recent/match-list/match-list.component';
import { HerogridComponent } from './heroes/herogrid/herogrid.component';
import { HeropageComponent } from './heropage/heropage/heropage.component';


const routes: Routes = [
  {
    path: 'matchDetails/:id',
    component: MatchdataComponent
  },
  {
    path: 'recentlyViewed',
    component: MatchListComponent
  },
  {
    path: 'heroes/:id',
    component: HeropageComponent
  }, 
  {
    path: 'heroes',
    component: HerogridComponent
  }, 
  {
    path: '**',
    component: SearchComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

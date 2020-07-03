import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { HttpClientModule } from '@angular/common/http'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeModule } from './home/home.module';
import { MatchdataModule } from './matchdata/matchdata.module';
import { RecentModule } from './recent/recent.module';
import { HeroesModule } from './heroes/heroes.module';
import { HeropageModule } from './heropage/heropage.module';
import { NavbarComponent } from './navbar/navbar.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
  ],
  imports: [
    BrowserModule,
    RecentModule,
    AppRoutingModule,
    HomeModule,
    MatchdataModule,
    HttpClientModule,
    HeroesModule,
    HeropageModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

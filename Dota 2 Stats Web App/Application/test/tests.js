const mocha = require("mocha");
const chai = require("chai");
const nock = require("nock");
const fs = require("fs");
const constants = require("dotaconstants");
const suite = mocha.suite;
const test = mocha.test;
const suiteSetup = mocha.suiteSetup;
const logic = require("../server/logic");

//suite("Travis Test", function() {
//    test("Check CI actually works", function() {
//        chai.assert.isTrue(true);
//    });
//});

suite("Match scoreboard tests", () => {
    let match;
    suiteSetup(() => {
        var rawData = fs.readFileSync(__dirname + "/mockdata/matchresponse.json");

        nock("https://api.opendota.com/api")
            .get("/matches/5149800717")
            .query(true)
            .reply(200, rawData);

        return new Promise(async (resolve) => {
            match = await logic.getMatchData(5149800717);
            resolve();
        });

    });

    test("Testing correct scores", () => {
        chai.assert.equal(match.dire.score, 18, "Dire score should equal 18");
        chai.assert.equal(match.radiant.score, 44, "Radiant score should equal 44");
    });
    test("Testing correct time", () => {
        chai.assert.equal(match.length, "35:08", "Match should be 2108 seconds long (35:08 Minutes)")
    });
    test("Testing team averages (XPM/GPM)", () => {
        chai.assert.equal(match.radiant.xpm, 663.6, "Radiants XPM should be 663.6");
        chai.assert.equal(match.radiant.gpm, 461, "Radiants GPM should be 461");
        chai.assert.equal(match.dire.xpm, 465, "Dires XPM should be 465");
        chai.assert.equal(match.dire.gpm, 335.8, "Dires GPM should be 335.8");
    });
    test("Testing match ID", () => {
        chai.assert.equal(match.id, 5149800717, "Match ID should be 5149800717")
    });
    test("Testing team victory", () => {
        chai.assert.isTrue(match.radiantVictory);
    });
    test("Testing radiant xp advantage", () => {
        chai.expect(match.xpAdvantage).to.not.equal(null);
        chai.expect(match.xpAdvantage.length).to.be.above(35);
        var values = [0, -23, -81, 64,-230,-190,-540,-408,485,-315,-181,-263,-14,315,-495,3206,3125,4569,6523,6181,10326,10360,11412,11274,16407,14456,13257,14861,17211,21625,25184,22368,23418,23244,30428,31876] 
         
        for (var i = 0; i < 10; i++) { 
            var randnum = Math.floor(Math.random() * match.xpAdvantage.length - 1);
 
            chai.expect(match.xpAdvantage[randnum]).to.equal(values[randnum]);
        }
     });
     test("Testing radiant gold advantage", () => { 
         chai.expect(match.goldAdvantage).to.not.equal(null);
         chai.expect(match.goldAdvantage.length).to.be.above(35);
         var values = [0,61,60,-411,-1099,-815,-1159,-1380,-1066,-1735,-1549,-442,-988,-823,-1164,940,1155,1694,2695,2375,5194,6348,6732,8177,10677,9692,9249,9774,10289,14174,17743,15971,15963,16384,18680,21968]
          
         for (var i = 0; i < 10; i++) { 
             var randnum = Math.floor(Math.random() * match.goldAdvantage.length - 1);
  
             chai.expect(match.goldAdvantage[randnum]).to.equal(values[randnum]);
         }
     });
});

suite("Testing match player stats", () => {
    suiteSetup(() => {
        var rawData = fs.readFileSync(__dirname + "/mockdata/matchresponse.json");

        nock("https://api.opendota.com/api")
            .get("/matches/5149800717")
            .query(true)
            .reply(200, rawData);

        return new Promise(async (resolve) => {
            match = await logic.getMatchData(5149800717);
            resolve();
        });

    });

    test("Testing radiant player names", () => {
        var players = match.radiant.players;
        var len = players.length;
        var radiantnames = ["pudding", "Thxnbai", "fisherman", "suuh dude", "Account private"];
        for (var i = 0; i < len; i++) {
            chai.expect(radiantnames).to.include(players[i].username);
        }
    });
    test("Testing dire player names", () => {
        var players = match.dire.players;
        var len = players.length;
        var direnames = ["J0k3r", "The Doctor", "Noah™", "Account private"];
        for (var i = 0; i < len; i++) {
            chai.expect(direnames).to.include(players[i].username);
        }
    });
    test("Testing KDA", () => {
        var player = match.radiant.players[0];
        chai.expect(player.kda).to.equal("2/9/24")
    });
    test("Testing GPM/XPM", () => {
        var player = match.dire.players[0];
        chai.expect(player.gpm).to.equal(428);
        chai.expect(player.xpm).to.equal(636);
    });
    test("Testing CS", () => {
       var totalLastHits = 0;
       var totalDenies = 0;
       
       var totalLastHitsTest = 0;
       var totalDeniesTest = 0;
       var players = match.radiant.players;
       var lastHits = [23, 230, 216, 42, 144];
       var denies = [1, 8, 32, 3, 13];

       for(var i = 0; i < players.length; i++) {
           var playerLH = players[i].lasthits;
           var playerD = players[i].denies;

           chai.expect(lastHits).to.contain(playerLH);
           chai.expect(denies).to.contain(playerD);

           totalLastHits += playerLH;
           totalDenies += playerD;

           totalLastHitsTest += lastHits[i];
           totalDeniesTest += denies[i];
       }

       chai.expect(totalLastHits).to.equal(totalLastHitsTest);
       chai.expect(totalDenies).to.equal(totalDeniesTest);
    });
    test("Testing players have items", () => {
        for (var i = 0; i < 5; i++) {
            chai.expect(match.radiant.players[i].items).does.not.equal(undefined);
            chai.expect(match.radiant.players[i].items).does.not.equal(null);
            chai.expect(match.dire.players[i].items).does.not.equal(undefined);
            chai.expect(match.dire.players[i].items).does.not.equal(null);
        }
    });
    test("Testing player has the correct items", () => {
        var playerItems = match.radiant.players[0].items;
        var actualItems = ["Arcane Boots", "Eul's Scepter of Divinity", "Glimmer Cape", "Ironwood Tree", "Magic Wand"];

        for (var i = 0; i < playerItems.length; i++) {
            chai.expect(actualItems).includes(playerItems[i].name);
        }
    });

});


suite("Testing hero data", async () => {
    let heroes = logic.getHeroesList();
    let matchups;
    let recents;
    let winTimes;

    suiteSetup(async() => {
        var rawData = fs.readFileSync(__dirname + "/mockdata/matchups.json");
        var rawDataRecentMatches = fs.readFileSync(__dirname + "/mockdata/matches.json");
        var rawDataWinrates = fs.readFileSync(__dirname + "/mockdata/durations.json");

        nock("https://api.opendota.com/api")
            .get("/heroes/3/matchups")
            .query(true)
            .reply(200, rawData);

        nock("https://api.opendota.com/api")
            .get("/heroes/5/matches")
            .query(true)
            .reply(200, rawDataRecentMatches);

        nock("https://api.opendota.com/api")
            .get("/heroes/3/durations")
            .query(true)
            .reply(200, rawDataWinrates);

        return new Promise(async (resolve) => {
            matchups = await logic.getHeroMatchups(3);
            recents = await logic.getHeroRecentMatches(5);
            winTimes = await logic.getHeroWinrates(3);
            resolve();
        });

    });

    test("Testing hero count", () => {
        chai.expect(heroes.length).to.equal(119);
    });
    test("Testing hero data", () => {
        var heroesTest = constants.heroes;
        var i = 0;

        for (hero in heroesTest) {
            chai.expect(heroes[i].name).to.equal(heroesTest[hero].localized_name);
            chai.expect(heroes[i].picture).to.equal(heroesTest[hero].img);
            chai.expect(heroes[i].attribute).to.equal(heroesTest[hero].primary_attr);
            i++;
        }
    });
    test("Testing matchups response", () => {
        chai.expect(matchups.heronames).to.not.equal(undefined);
        chai.expect(matchups.winrates).to.not.equal(undefined);
        chai.expect(matchups.winrates.length).to.equal(116);
        chai.expect(matchups.heronames.length).to.equal(116);
    });
    test("Testing match up stats", () => {
        chai.expect(matchups.heronames[3]).to.equal("Shadow Shaman");
        chai.expect(Math.floor(matchups.winrates[3])).to.equal(53);
    });
    test("Testing hero recent matches", () => {
        chai.expect(recents[0].id).to.equal(5177292561);
        chai.expect(recents[0].length).to.equal("43:23");
        chai.expect(recents[0].victory).to.be.false;
        chai.expect(recents[0].kda).to.equal("1/12/20");
    });
    test("Testing match duration winrates response", () => {
        chai.expect(winTimes.winrates).to.not.equal(undefined);
        chai.expect(winTimes.winrates.length).to.equal(15);
    });
    test("Testing match duration winrates", () => {
        chai.expect(winTimes.durations[0]).to.equal(5);
        chai.expect(Math.floor(winTimes.winrates[0])).to.equal(81);
        chai.expect(winTimes.durations[4]).to.equal(25);
        chai.expect(Math.floor(winTimes.winrates[4])).to.equal(54);
        chai.expect(winTimes.durations[9]).to.equal(50);
        chai.expect(Math.floor(winTimes.winrates[9])).to.equal(50);
    });
    test("Testing match duration winrates sorting", () => {
        for (var i = 1; i < 15; i++) {
            chai.expect(winTimes.durations[i]).is.above(winTimes.durations[i -1])
        }
    });
});
const KEY = "insertopendotakey";
const OPENDOTA = "https://api.opendota.com/api/";

const request = require("request");
const constants = require("dotaconstants");

async function getHeroWinrates(id) {
    var url = OPENDOTA + "heroes/" + id + "/durations" + KEY;
    var data = await(apiRequest(url));

    //Sort by match duration
    data.sort((a , b) => a.duration_bin - b.duration_bin);

    var response = {
        durations : [],
        winrates : []
    };

    var length = data.length < 15 ? data.length : 15;

    for (var i = 0; i < length; i++) {
        response.durations.push(data[i].duration_bin / 60);
        var winrate = Math.floor((data[i].wins / data[i].games_played) * 100);
        response.winrates.push(winrate);
    }

    return response;
}

function getHeroData(id) {
    var hero = constants.heroes[id];

    heroData = {
        name : hero.localized_name,
        attribute : getAttributeString(hero.primary_attr),
        roles : hero.roles,
        attackType : hero.attack_type,
        legs : hero.legs,
        picture : hero.img
    }

    return heroData;
}

async function getHeroMatchups(id) {
    var url = OPENDOTA + "heroes/" + id + "/matchups" + KEY;
    var data = await apiRequest(url);
    var allHeroes = constants.heroes;


    var response = {
        name : allHeroes[id].localized_name,
        heronames : [],
        winrates : []
    }

    for (var i = 0; i < data.length; i++) {
        var hero = data[i];
        var heroname = allHeroes[hero.hero_id].localized_name;
        var winrate = (hero.wins / hero.games_played) * 100;
        response.heronames.push(heroname);
        response.winrates.push(winrate);
    }

    return response;
}
async function getHeroRecentMatches(id) {
    var url = OPENDOTA + "heroes/" + id + "/matches" + KEY; 
    var data = await apiRequest(url);

    var matchList = [];
    var numMatches = 10 > data.length ? data.length : 10;
    for (var i = 0; i < numMatches; i++) { 
        var thisMatch = {
            id: data[i].match_id,
            length: secToMins(data[i].duration), 
            kda: kdaString(data[i].kills, data[i].deaths, data[i].assists),
            victory: determineVictory(data[i].radiant, data[i].radiant_win)
        }

        matchList.push(thisMatch);
    }
    return matchList;
}
function getHeroesList() {
    var allHeroes = constants.heroes;
    var heroesList = [];

    for (hero in allHeroes) {
        var thisHero = {
            id : allHeroes[hero].id,
            name : allHeroes[hero].localized_name,
            picture : allHeroes[hero].img,
            attribute : allHeroes[hero].primary_attr

        }
        heroesList.push(thisHero);
    } 

    return heroesList;
}

async function getMatchData(matchID) {
    var url = OPENDOTA + "matches/" + matchID + KEY;
    var data = await apiRequest(url);

    if (data.match_id != undefined)
        var matchData = parseMatchData(data);
    else 
        var matchData = null;

    return matchData;
}

function parseMatchData(data) {
    var team1 = getTeamStats("Radiant", data);
    var team2 = getTeamStats("Dire", data)

    var match = {
        id: data.match_id,
        radiantVictory: data.radiant_win,
        length: secToMins(data.duration),
        radiant: team1,
        dire: team2,
        xpAdvantage: data.radiant_xp_adv,
        goldAdvantage: data.radiant_gold_adv
    }

    return match;
} 

function getTeamStats(team, data) {
    var teamstats = new Object();
    if (team == "Radiant") {
        teamstats.name = data.radiant_team == undefined ? team : data.radiant_team.name;
        teamstats.score = data.radiant_score;
    } else {
        teamstats.name = data.dire_team == undefined ? team : data.dire_team.name;
        teamstats.score = data.dire_score;
    }
    
    teamstats.players = getTeamPlayers(team, data);
    teamstats.lasthits = 0;
    teamstats.denies = 0;
    var xpm = 0; 
    var gpm = 0;
    for (var i = 0; i < teamstats.players.length; i++) 
    {
        xpm += teamstats.players[i].xpm;
        gpm += teamstats.players[i].gpm;
        teamstats.lasthits += teamstats.players[i].lasthits;
        teamstats.denies += teamstats.players[i].denies;
    }
    teamstats.totalgpm = gpm;
    teamstats.totalxpm = xpm;
    teamstats.totalkda = getTeamKdaString(teamstats.players);

    xpm /= teamstats.players.length;
    gpm /= teamstats.players.length;

    teamstats.gpm = gpm;
    teamstats.xpm = xpm;

    return teamstats;
}

function getTeamPlayers(team, data) {
    var allPlayers = data.players;
    var teamPlayers = [];
    if (team === "Radiant") {
        for (var i = 0; i < allPlayers.length; i++) {
            if(allPlayers[i].player_slot <= 127)
            {
                var player = getPlayerStats(allPlayers[i]);
                teamPlayers.push(player);
            }
        }
    } else {
        for (var i = 0; i < allPlayers.length; i++) {
            if(allPlayers[i].player_slot > 127)
            {
                var player = getPlayerStats(allPlayers[i]);
                teamPlayers.push(player);
            }
        }
    }

    return teamPlayers;
}

function getPlayerStats(player) {
    var person  = new Object();
    person.account = player.account_id === undefined ? -1 : player.account_id;
    person.username = getUsername(player);
    person.gpm = player.gold_per_min;
    person.xpm = player.xp_per_min;
    person.denies = player.denies;
    person.lasthits = player.last_hits;
    person.kills = player.kills;
    person.deaths = player.deaths;
    person.assists = player.assists;
    person.kda = getPlayerKdaString(player);
    person.hero = getHero(player.hero_id);
    person.items = getPlayerItems(player);
    return person;
}

function getUsername(player) {
    if (player.name != null || player.name != undefined) //If avaiable, use pro/tournament name
        return player.name;
    else if (player.personaname != null || player.personaname != undefined) //If not, use steam name
        return player.personaname;
    else
        return "Account private";        //If both are null, the account isn't exposing match data, so return Account Private
}

function getHero(id) {
    var hero = new Object();
    var temp = constants.heroes[id];

    hero.name = temp.localized_name;
    hero.picture = temp.icon;

    return hero;
}

function getTeamKdaString(players) {
    var kills = 0;
    var deaths = 0;
    var assists = 0;
    for (var i = 0; i < players.length; i++) {
        kills += players[i].kills;
        deaths += players[i].deaths;
        assists += players[i].assists;
    }

    return kdaString(kills, deaths, assists);
}


function getPlayerKdaString(player) {
    var kills = player.kills;
    var death = player.deaths;
    var assist = player.assists;

    return kdaString(kills, death, assist);
}

function kdaString(kills, deaths, assists) {
    return kills + "/" + deaths + "/" + assists;
} 

function apiRequest(url) {
    return new Promise((resolve, reject) => {
        request(url, { json: true }, (err, res, body) => {
            if (!err)
                resolve(body);
            else
                reject(err);
        });
    })
} 

function secToMins(time) {
    var min = Math.floor(time / 60);
    var sec = time - min * 60;
    var secStr = (sec > 10) ? sec : "0" + sec;
    return min + ":" + secStr;
}

function determineVictory(playerIsRadiant, radiantVictory) {
    if(playerIsRadiant && radiantVictory) {
        return true;
    } else if (!playerIsRadiant && radiantVictory) {
        return false;
    } else if (playerIsRadiant && !radiantVictory) {
        return false;
    } else {
        return true;
    }
}

function getAttributeString(attribute) {
    var attrString;

    switch(attribute) {
        case "agi":
            attrString = "Agility";
            break;
        case "str": 
            attrString = "Strength";
            break;
        case "int":
            attrString = "Intelligence";
            break;
        default:
            attrString = "No Attribute";
            break;
    }

    return attrString;
}

function getPlayerItems(player) {
    var items = [];

    if(player.item_0 != 0) 
        items.push(getItem(player.item_0));
    if(player.item_1 != 0) 
        items.push(getItem(player.item_1));
    if(player.item_2 != 0) 
        items.push(getItem(player.item_2));
    if(player.item_3 != 0) 
        items.push(getItem(player.item_3));
    if(player.item_4 != 0) 
        items.push(getItem(player.item_4));
    if(player.item_5 != 0) 
        items.push(getItem(player.item_5));

    return items;
}

function getItem(itemId) {
    var itemCode = constants.item_ids[itemId];
    var item = {
        name : constants.items[itemCode].dname,
        picture : constants.items[itemCode].img 
    }

    return item;
}

module.exports.getMatchData = getMatchData;
module.exports.getHeroesList = getHeroesList;
module.exports.getHeroMatchups = getHeroMatchups;
module.exports.getHeroRecentMatches = getHeroRecentMatches;
module.exports.getHeroData = getHeroData;
module.exports.getHeroWinrates = getHeroWinrates;
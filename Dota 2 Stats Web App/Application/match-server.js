const express = require("express");
const http = require("http");
const logic = require("./server/logic");
const db = require("./server/db");
const path = require("path");
const app = express();
const server = http.createServer(app);
const io = require("socket.io")(server);
var bodyParser = require('body-parser');

app.use(express.static(__dirname + "/server/dist"));
app.use(bodyParser.json());

const PORT = 8080;

const recentNsp = io.of('/recent-namespace');
recentNsp.on('connection', (socket) => {
    console.log("User connected to recent matches");

    socket.on('disconnect', () => {
        console.log("Disconnected from recent matches");
    });
});

const commentNsp = io.of('/comment-namespace');
commentNsp.on('connection', (socket) => {
    console.log("User connected to comments");

    socket.on('disconnect', () => {
        console.log("Disconnected from comments");
    });

    socket.on('join', (matchId) => {
        socket.join("room-" + matchId);
    });

    socket.on('leave', (matchId) => {
        socket.leave("room-" + matchId);
    })

    socket.on('comment', async (comment) => {

        if(comment.body.trim() != "" && comment.name.trim() != "") {
            var toAdd = new db.comment({
                matchId : comment.matchId,
                name : comment.name,
                timePosted : new Date(),
                body : comment.body
            });

            await toAdd.save();
            commentNsp.in("room-" + comment.matchId).emit('new-comment');
        }  
    });
});

server.listen(PORT, () => {
    console.log("Server listening on port: " + PORT);
});

app.get("/api/match/:id", async (request, response) => {
    var id = request.params.id;
    var matchData = await logic.getMatchData(id);

    if(matchData != null) {
        var thisMatch = new db.match({
            matchId: id,
            access: new Date()
        });

        thisMatch.save();
        recentNsp.emit('match-viewed');

        response.status(200).send(matchData);
    } else {
        response.status(404).send("Not found");
    }
});

app.get("/api/match/:id/comments", async  (request, response) => {
    var id = request.params.id;
    var comments = await db.comment.find({matchId : id}).sort({timePosted : -1});

    response.status(200).send(comments);
});

app.get("/api/recentlyViewed", async (request, response) => {
    var found = await db.match.find().sort({access : -1}).limit(10);
    response.status(200).send(found);
});

app.get("/api/recentCount", async (request, response) => {
    var counts = await db.match.aggregate([
        { "$group": { _id: "$matchId", count: { $sum: 1 } } },
        { "$sort": { count: -1 } },
        { "$limit": 5 }
    ]).exec();

    response.status(200).send(counts);
});

app.get("/api/allHeroes", async (request, response) => {
    var heroes = await logic.getHeroesList();

    response.status(200).send(heroes);
});

app.get("/api/:heroId/matchups", async (request, response) => {
    var id = request.params.heroId;
    var heroes = await logic.getHeroMatchups(id);

    response.status(200).send(heroes);
});

app.get("/api/:heroId/matches", async (request, response) => {
    var id = request.params.heroId;
    var heroes = await logic.getHeroRecentMatches(id);

    response.status(200).send(heroes);
});

app.get("/api/hero/:heroId", async (request, response) => {
    var id = request.params.heroId;
    var hero = await logic.getHeroData(id);

    response.status(200).send(hero);
});

app.get("/api/:heroId/winrates", async (request, response ) => {
    var id = request.params.heroId;
    var winrates = await logic.getHeroWinrates(id);

    response.status(200).send(winrates);
});

//Catch any other routes and send them to the home page or an angular route.
app.get("*", function(request, response){
    response.sendFile(path.join(__dirname + "/server/dist/index.html"));
});

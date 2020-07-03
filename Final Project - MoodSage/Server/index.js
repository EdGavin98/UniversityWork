//Dependencies
const express = require("express");
const bodyParser = require("body-parser");
const cors = require("cors");
const mongoose = require("mongoose");
const config = require("./config/config");
const path = require("path")

//Route files
const api = require("./routes/api");
const auth = require("./routes/auth");

//Passport setup
const passport = require("passport");
require("./config/passport");

//Connect db
const env = process.env.NODE_ENV || "development";
const connString = config[env.trim()].db;
console.log(connString);
mongoose.connect(connString, {useNewUrlParser : true, useUnifiedTopology: true, useFindAndModify : false});

mongoose.connection.once('open', () => { 
    app.emit('ready'); 
});

//App setup
const app = express();
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.static(__dirname + "/dist/WebApp"));
app.use(cors());

//Routers
app.use("/Api", passport.authenticate('jwt', {session : false}), api);
app.use("/Auth", auth);

const PORT = process.env.PORT || 8080;

const server = app.on('ready', function() { 
    app.listen(PORT, function(){ 
        console.log(`Listening on port ${PORT}`); 
    }); 
}); 

app.get("*", async (request, response) => {
    response.sendFile(path.join(__dirname + "/dist/WebApp/index.html"));
});

module.exports = server;
var mongoose = require("mongoose");
const uri = "insertdbconnstring";


mongoose.connect(uri, {useNewUrlParser: true,
                useUnifiedTopology: true});

var matchSchema = mongoose.Schema({
    matchId: String, //Not unique
    access: Date,
});

var commentSchema = mongoose.Schema({
    matchId: String,
    name: String, 
    timePosted: Date,
    body: String
});

var match = mongoose.model("Match", matchSchema);
var comment = mongoose.model("Comment", commentSchema);

module.exports.comment = comment;
module.exports.match = match;
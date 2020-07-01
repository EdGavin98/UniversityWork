const mongoose = require("mongoose");

var moodSchema = mongoose.Schema({
    user : {
        type : mongoose.Schema.Types.ObjectId,
        ref : "Accounts"
    },
    date : {
        type: Date,
        required: true,
        default: Date.now
    },
    rating : {
        type: Number,
        required: true,
        min: 1,
        max: 10
    },
    comment : {
        type : String,
        maxlength : 250
    },
    private : {
        type : Boolean,
        required : true,
        default : false
    }
});

var moodModel =  mongoose.model("Moods", moodSchema);
module.exports.Mood =  moodModel;

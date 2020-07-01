const mongoose = require("mongoose");

var worrySchema = mongoose.Schema({
    user : {
        type : mongoose.Schema.Types.ObjectId,
        ref : "Accounts"
    },
    date : {
        type : Date,
        required: true,
        default: Date.now
    },
    severity : {
        type: Number,
        required: true,
        min : 1,
        max : 10
    },
    description : {
        type : String,
        maxLength : 500
    },
    type : {
        type : String,
        required : true,
        enum : ["Current", "Hypothetical"]
    },
    private : {
        type : Boolean,
        required : true,
        default : false
    },
    solutions : [{
        timeLogged : {
            type : Date,
            required: true,
            default: Date.now
        },
        description : {
            type : String,
            maxLength : 250,
            required: true
        },
        advantages : {
            type : String,
            maxLength : 500
        },
        disadvantages : {
            type : String,
            maxLength : 500
        }
    }]
})


var worryModel = mongoose.model("Worries", worrySchema);
module.exports.Worry = worryModel;


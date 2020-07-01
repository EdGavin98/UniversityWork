const mongoose = require("mongoose");
const Account = require("./account").Account;
const Worry = require("./worry").Worry;
const Mood = require("./mood").Mood;
const Therapist = require("./therapist").Therapist;

const userSchema = new mongoose.Schema({
    linkedAccounts : [{
        user : {
            type : mongoose.Schema.Types.ObjectId,
            ref : 'Therapists',
            required : true 
        },
        status : {
            type : Number,
            default : 0,
            enum : [0,1] // 0 - Pending, 1 - Accepted. Account links will be deleted on rejection/removal
        }
    }],
    moodTarget : {
        type : Number,
        min : 1,
        max : 10
    },
    worryTarget : {
        type : Number,
        min : 1,
        max : 10
    }
},
{
    toJSON : {
        virtuals : true
    },
    toObject : {
        virtuals : true
    }
});

userSchema.virtual('moods', {
    ref : "Moods",
    localField : "_id",
    foreignField : "user",
    justOne : false
});

userSchema.virtual('worries', {
    ref : "Worries",
    localField : "_id",
    foreignField : "user",
    justOne : false
});

userSchema.pre('remove', async function(next) {
    var removedUser = this;
    await Worry.deleteMany({user : removedUser._id})
    await Mood.deleteMany({user : removedUser._id})
    for (const link of removedUser.linkedAccounts) {
        await Therapist.updateOne(
            {_id : link.user._id},
            {$pull : { linkedAccounts : {user : removedUser._id }}},
            {safe : true}
        )
    }
    next()
});

var userDiscriminator = Account.discriminator('Users', userSchema);

module.exports.User = userDiscriminator;


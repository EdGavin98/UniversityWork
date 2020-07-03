const mongoose = require("mongoose");

const Account = require("../../database/account").Account;
const Mood = require("../../database/mood").Mood;
const Worry = require("../../database/worry").Worry;
const User = require("../../database/user").User;
const Therapist = require("../../database/therapist").Therapist;

async function beginPopulation() {
    await Account.deleteMany({});
    await Mood.deleteMany({});
    await Worry.deleteMany({});
    await addUsers();
}

async function addUsers() {

    firstUser = new User({
        forename : "TestUser",
        surname: "Test",
        email : "test@user.com",
        role : "patient",
        password : "bob"
    })
    await firstUser.save();

    secondUser = new Therapist({
        forename : "TestTherapist",
        surname : "Test2",
        email : "test@therapist.com",
        role : "therapist",
        password: "pass"
    })
    await secondUser.save()
    
    await addMoodsForUser(firstUser._id);
    await addWorriesForUser(firstUser._id);
}


async function addMoodsForUser(userId) {
    var mood1 = new Mood({
        user : userId,
        rating : 5,
        comment : "Hello",
        date : new Date(2012, 2, 21)
    })

    var mood2 = new Mood({
        user : userId,
        rating : 6,
        comment : "Hello2",
        date : new Date(2020, 2, 22)
    })

    var mood3 = new Mood({
        user : userId,
        rating : 7,
        comment : "Hello3",
        date : new Date(2020, 2, 23)
    })

    var mood4 = new Mood({
        user : userId,
        rating : 8,
        comment : "Hello4",
        date : new Date(2020, 2, 24)
    })

    await mood1.save();
    await mood2.save();
    await mood3.save();
    await mood4.save();

}

async function addWorriesForUser(userId) {

    var worry1 = new Worry({
        user : userId,
        severity : 5,
        description : "Test description",
        type : "Hypothetical",
        date : new Date(2020, 2, 22)
    })

    var worry2 = new Worry({
        user : userId,
        severity : 8,
        description : "Test description 2",
        type : "Current",
        date : new Date(2020, 2, 23)
    })

    await worry1.save();
    await worry2.save();
}

module.exports.beginPopulation = beginPopulation;


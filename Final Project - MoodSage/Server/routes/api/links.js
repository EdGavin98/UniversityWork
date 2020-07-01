const Account = require("../../database/account").Account;
const Therapist = require("../../database/therapist").Therapist;
const User = require("../../database/user").User;


function getMyLinks(req, res, next) {
    var id = req.user._id;
    Account.findOne({ _id : id }).populate("linkedAccounts.user", "-linkedAccounts -role -__v -type")
    .then(result => {
      res.send(result.linkedAccounts)
    })
    .catch(error => {
      console.log(error)
      res.status(400).send();
    })
}

function addNewLink(req, res, next) {

    var patient = req.body.email;
    var therapist = { user : req.user._id };
  
    User.findOneAndUpdate(
        {email : patient},
        {$push : { linkedAccounts : therapist }}
    )
    .then(result => {
        Therapist.updateOne(
            { _id : req.user._id },
            {$push : { linkedAccounts : {user : result._id}}}
        )
        .then(nextResult => {
            res.status(201).send();
        })
        .catch(error => {
            res.status(500).send();
        });
    })
    .catch(error => {
        res.status(404).send();
    });
}

function acceptLink(req, res, next) {
    var patient = req.user._id;
    var therapist = req.body.user;

    if (therapist != req.params.user || !therapist) {
        res.status(400).send();
    } else {
        User.updateOne(
            {_id : patient, "linkedAccounts.user" : therapist },
            { $set : { "linkedAccounts.$.status" : 1}}
        )
        .then(result => {
            Therapist.updateOne(
                {_id : therapist, "linkedAccounts.user" : patient},
                {$set : {"linkedAccounts.$.status" : 1}},
            )
            .then(nextResult => {
                res.status(201).send()
            })
            .catch(error => {
                res.status(500).send();
            });
        })
        .catch(result => {
            res.status(404).send();
        })
    }
}

function removeLink(req, res, next) {
    var patient;
    var therapist;
    if (req.user.role === "patient") {
        patient = req.user._id;
        therapist = req.params.user;
    } else {
        patient = req.params.user;
        therapist = req.user._id;
    }

    Therapist.updateOne(
        {_id : therapist},
        {$pull : { linkedAccounts : {user : patient}}}
    )
    .then(result => {
        User.updateOne(
            {_id :  patient},
            {$pull : {linkedAccounts : {user : therapist}}}
        )
        .then(result => {
            res.status(204).send();
        })
        .catch(error => {
            res.status(500).send()
        })
    })
    .catch(error => {
        res.status(404).send()
    })
}

module.exports.addNewLink = addNewLink
module.exports.acceptLink = acceptLink
module.exports.removeLink = removeLink
module.exports.getMyLinks = getMyLinks
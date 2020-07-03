const Mood = require("../../database/mood").Mood;

function addNewMood(req, res, next) {
    var body = req.body;

    var toAdd = new Mood(body);

    toAdd.save()
    .then(result => {
        res.status(201).send();
    })
    .catch(error => {
        res.status(400);
    });
}

function getMoodsAsPatient(req, res, next) {
    var id = req.user._id;

    Mood.find({ user : id })
    .then(result => {
      res.status(200).send(result)
    })
    .catch(error => {
      res.status(404).send()
    }); 
}

function getMoodsAsPatientAfterDate(req, res, next) {
    var earliestDate = new Date(req.params.year, req.params.month - 1, req.params.day);
  
    Mood.find({
      user : req.user._id,
      date : { "$gte" : earliestDate }
    })
    .then(result => {
      res.status(200).send(result);
    })
    .catch(error => {
      res.status(404).send();
    });
}

function deleteMood(req, res, next) {
  var moodDate = new Date(req.params.date);
  var id = req.user._id;

  Mood.deleteOne({user : id, date : moodDate})
  .then(result => {
    res.status(204).send()
  })
  .catch(error => {
    res.status(500).send()
  })
}

module.exports.addNewMood = addNewMood;
module.exports.getMoodsAsPatient = getMoodsAsPatient
module.exports.getMoodsAsPatientAfterDate = getMoodsAsPatientAfterDate
module.exports.deleteMood = deleteMood
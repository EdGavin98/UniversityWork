const Worry = require("../../database/worry").Worry;

function getWorriesAsPatient(req, res, next) {
    const id = req.user._id;

    Worry.find({ user : id })
    .then(result => {
      res.status(200).send(result)
    })
    .catch(error => {
      res.status(500).send()
    }); 
}

function addNewWorry(req, res, next) {
    var worry = new Worry(req.body);

    worry.save()
    .then(result => {
        res.status(201).send();
    })
    .catch(error => {
        res.status(400);
    });
}

function addSolutionToWorry(req, res, next) {
  var solutionDate = new Date(req.params.date);
  var id = req.user._id;

  Worry.updateOne(
    {user : id, date : solutionDate},
    {$push: {solutions : req.body}}
  ).then((result) => {
    if (result.n === 1) {
      res.status(204).send()
    } else {
      res.status(404).send()
    }
  })
  .catch((error) => {
    console.log(error)
    res.status(400).send()
  })
  
}

function deleteWorry(req, res, next) {
  var worryDate = new Date(req.params.date);
  var id = req.user._id;

  Worry.deleteOne({user : id, date : worryDate})
  .then(result => {
    res.status(204).send()
  })
  .catch(error => {
    res.status(500).send()
  })
}

module.exports.getWorriesAsPatient = getWorriesAsPatient
module.exports.addNewWorry = addNewWorry
module.exports.addSolutionToWorry = addSolutionToWorry
module.exports.deleteWorry = deleteWorry
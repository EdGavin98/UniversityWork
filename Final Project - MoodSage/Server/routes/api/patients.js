const User = require("../../database/user").User;

async function getPatientDataAsTherapist(req, res, next) {
    var id = req.params.id;

    if (req.user.linkedAccounts.find(link => id == link.user && link.status == 1)) {
      User.findById(id)
      .populate({path: "moods", select : "-_id -__v", match : {private : false}})
      .populate({path: "worries", select : "-_id -__v", match : {private : false}})
      .then(result => {
        if (result.role == "patient") {
          res.status(200).json(result)
        } else {
          res.status(404).send({"error" : "This account is not a patient"});
        }
      })
      .catch(error => {
        res.status(404)
      })
    } else {
      res.status(403).send();
    }
}

module.exports.getPatientDataAsTherapist = getPatientDataAsTherapist

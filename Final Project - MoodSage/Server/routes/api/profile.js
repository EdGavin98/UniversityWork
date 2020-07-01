const Account = require("../../database/account").Account;
const User = require("../../database/user").User;
const Therapist = require("../../database/therapist").Therapist;

function getProfile(req, res, next) {
    res.send(req.user);
}

function updateProfile(req, res, next) {
  var id = req.user._id;
  var userType;
  if (req.user.role === "patient") {
    userType = User;
  } else {
    userType = Therapist;
  }

  userType.updateOne({_id : id}, req.body)
  .then((result) => {
    res.status(204).send()
  })
  .catch((error) => {
    res.status(400).send()
  })

}

function deleteProfile(req, res, next) {
  var id = req.user._id;
  Account.findById(id)
  .then((user) => {
    user.remove()
    .then((doc) => {
      res.status(204).send()
    })
  })
  .catch(error => {
    res.status(500).send()
  })
}


module.exports.getProfile = getProfile;
module.exports.deleteProfile = deleteProfile;
module.exports.updateProfile = updateProfile;
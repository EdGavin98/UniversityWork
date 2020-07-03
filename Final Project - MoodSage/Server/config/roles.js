//Function to check the users roles
const checkUserHasRole = (...roles) => (req, res, next) => {
    var correctRole = roles.find(role => role === req.user.role);
  
    if (correctRole)
      next();
    else 
      res.status(401).send({"Error" : "Incorrect Roles"});
  }

module.exports.checkUserHasRole = checkUserHasRole;
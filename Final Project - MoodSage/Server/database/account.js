const mongoose = require("mongoose");
const bcrypt = require("bcrypt");

var accountSchema = mongoose.Schema({
    forename : {
        type: String,
        required: true
    },
    surname : {
        type: String,
        required: true
    },
    email : {
        type: String,
        required: true,
        unique: true
    },
    password : {
        type: String,
        required: true
    },
    role : {
        type: String,
        required: true,
        enum : ["therapist", "patient"]
    }
}, 
{
    discriminatorKey: 'type',
}
);

//Pre save hook to 0hash the password before storing it in the database
accountSchema.pre('save', function(next) {
    var user = this;
    if (!user.isModified('password'))
        return next()

    bcrypt.genSalt(12, (err, salt) => {
        if (err)
            return next()

        bcrypt.hash(user.password, salt, (err, hash) => {
            if (err) return next(err);

            user.password = hash;
            next()
        })
    })
});

// Small helper function to validate the users password against the provided one
//
// Inputs: 
//      None
// Return:
//      Boolean - Do the passwords match
accountSchema.methods.validatePassword =  async function(password) {
    return await bcrypt.compare(password, this.password);
}

// Delete the passsword of the user before sending it as a json response
accountSchema.methods.toJSON = function() {
    var user = this.toObject();
    delete user.password
    return user
}

var accountModel = mongoose.model("Accounts", accountSchema);
module.exports.Account = accountModel;


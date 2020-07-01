const passport = require("passport");
const LocalStrategy = require("passport-local").Strategy;
const JWTStrategy = require("passport-jwt").Strategy;
const Account = require("../database/account").Account;
const ExtractJWT = require("passport-jwt").ExtractJwt;

passport.use ('login', new LocalStrategy({
    usernameField: 'email',
    passwordField: 'password'
}, async (email, password, done) => {
    Account.findOne({"email" : email})
    .then(async (user) => {
        var valid = await user.validatePassword(password)
        if (!user || !valid) {
            return done(null, false, {errors : {"Email or Password" : "Invalid"}})
        } else {
            return done(null, user)
        }
    }).catch(done)
}));

passport.use('jwt', new JWTStrategy({
    jwtFromRequest : ExtractJWT.fromAuthHeaderAsBearerToken(),
    secretOrKey    : 'seacrits'    
    },
    async function (jwtPayload, done) {
        return Account.findById(jwtPayload.user._id)
            .then(user => {
                return done(null, user)
            }) 
            .catch(err => {
                return done(err)
            })
    }

));
const express = require('express');
const passport = require("passport");
const Account = require("../database/account").Account;
const User = require("../database/user").User;
const Therapist = require("../database/therapist").Therapist;
const jwt = require("jsonwebtoken");
const router = express.Router();

router.post("/register", (request, response) => {
    var details = request.body
    var user;
    if (details.role === "patient") {
        user = new User(details);
    } else {
        user = new Therapist(details)
    }
    
    user.save((error, account) => {
        if (error) return response.status(409).json(error)

        response.status(200).send({ message : "Account created successfully"});
    });
});

router.post("/login", (request, response, next) => {
    passport.authenticate('login', async (err, user, info) => {     
        try {
            if(err || !user){
                const error = new Error('An Error occurred')
                return next(error);
            }
            request.login(user, { session : false }, async (error) => {
                if (error) 
                    return next(error)
                
                const body = { _id : user._id};
                const token = jwt.sign({ user : body },'seacrits');

                return response.json({ token });
            });     
        } catch (error) {
            return next(error);
        }
    })(request, response, next);
});

module.exports = router;
const mocha = require("mocha");
const describe = mocha.describe;
const it = mocha.it;
const beforeEach = mocha.beforeEach
const after = mocha.after;
const request = require("supertest");
const chai = require("chai");
const helper = require("./helper/db-setup")

var app = require('../index');
var agent = request.agent(app);

describe("App tests", () => {
    var therapistToken;
    var userToken;

    beforeEach(() => {
        return new Promise(async (resolve) => {
            await helper.beginPopulation();

            agent.post("/auth/login")
            .send({
                email : "test@therapist.com",
                password : "pass"
            })
            .end((err, response) => {
                therapistToken = `Bearer ${response.body.token}`
                agent.post("/auth/login")
                .send({
                    email : "test@user.com",
                    password : "bob"
                })
                .end((err, response) => {
                    userToken = `Bearer ${response.body.token}`
                    resolve();
                })
            })
        });
    });


////////////////// Tests /////////////////////////////////////

    it("Testing profile retrieval", (done) => {
        agent.get("/api/profile")
        .set("Authorization", therapistToken)
        .expect(200)
        .expect((res) => {
            chai.expect(res.body.email).equals("test@therapist.com")
        })
        .end(() => {

            agent.get("/api/profile")
            .set("Authorization", userToken)
            .expect(200)
            .expect((res) => {
                chai.expect(res.body.email).equals("test@user.com")
            })
            .end(done)
        })
    });
    it("Testing profile retrieval with no token", (done) => {
        agent.get("/api/profile")
        .expect(401, done)
    });
    it("Testing link flow (Therapist add, user accept, user remove)", (done) => {
        agent.get("/api/profile")
        .set("Authorization", therapistToken)
        .expect(200)
        .end((error, res) => {
            //Begin therapist add patient
            var therapistId = res.body._id;
            agent.post("/api/profile/links")
            .set("Authorization", therapistToken)
            .send({email : "test@user.com"})
            .expect(201)
            .end(() => {
                //Begin patient add therapist
                agent.put(`/api/profile/link/${therapistId}`)
                .send({user : therapistId})
                .set("Authorization", userToken)
                .expect(201)
                .end(() => {
                    //Begin patient delete therapist
                    agent.delete(`/api/profile/link/${therapistId}`)
                    .set("Authorization", userToken)
                    .expect(204)
                    .end((err, res) => {
                        done(err);
                    })
                })
            });
        })
    });
    it("Testing link add with incorrect user", (done) => {
        agent.post("/api/profile/links")
        .set("Authorization", therapistToken)
        .send({email : "bob@hope.com"})
        .expect(404, done)
    });
    it("Testing link add with wrong role", (done) => {
        agent.post("/api/profile/links")
        .set("Authorization", userToken)
        .expect(401, done)
    })
    it("Testing link accept for non-existant id", (done) => {
        agent.post("/api/profile/links")
        .set("Authorization", therapistToken)
        .send({email : "test@user.com"})
        .expect(201)
        .end(() => {
           agent.put("/api/profile/link/notarealid")
           .set("Authorization", userToken)
           .send({ user : "notarealid"})
           .expect(404, done)
        });
    });
    it("Testing user retrieval of moods", (done) => {
        agent.get("/api/profile/moods")
        .set("Authorization", userToken)
        .expect(200)
        .expect((res)=> {
            chai.expect(res.body.length).equals(4)
        })
        .end(done);
    });
    it("Testing therapist retrieval of a user mood - unlinked", (done) => {
        //Get user id for retrieval
        agent.get("/api/profile")
        .set("Authorization", userToken)
        .end((err, res) => {
            var userId = res.body._id;

            agent.get(`/api/patient/${userId}`)
            .set("Authorization", therapistToken)
            .expect(403, done)
        });
    });
    it("Testing therapist retrieval of user mood - linked", (done) => {
        agent.get("/api/profile")
        .set("Authorization", therapistToken)
        .expect(200)
        .end((error, res) => {
            //Begin therapist add patient
            var therapistId = res.body._id;
            agent.post("/api/profile/links")
            .set("Authorization", therapistToken)
            .send({email : "test@user.com"})
            .expect(201)
            .end(() => {
                //Begin patient add therapist
                agent.put(`/api/profile/link/${therapistId}`)
                .send({user : therapistId})
                .set("Authorization", userToken)
                .expect(201)
                .end(() => {

                    agent.get("/api/profile")
                    .set("Authorization", userToken)
                    .end((err, res) => {
                        var userId = res.body._id;
                        agent.get(`/api/patient/${userId}`)
                        .set("Authorization", therapistToken)
                        .expect(200)
                        .expect((res) => {
                            chai.expect(res.body.moods.length).to.equal(4);
                            chai.expect(res.body.worries.length).to.equal(2);
                        })
                        .end(done)
                    })
                })
            })
        })
    })
    it("Testing user add mood", (done) => {
        agent.get("/api/profile")
        .set("Authorization", userToken)
        .expect(200)
        .end((error, res) => {
            
            agent.post("/api/profile/moods")
            .set("Authorization", userToken)
            .send({
                user : res.body._id,
                rating : 8,
                comment : "New test",
                date : Date(2020, 2, 15)
            })
            .expect(201, done)
        });
    });
    it("Testing user get worries", (done) => {
        agent.get("/api/profile/worries")
        .set("Authorization", userToken)
        .expect(200)
        .expect((res) => {
            chai.expect(res.body.length).to.equal(2);
        })
        .end(done);
    });
    it("Testing user add worry", (done) => {
        agent.get("/api/profile")
        .set("Authorization", userToken)
        .expect(200)
        .end((error, res) => {
            
            agent.post("/api/profile/worries")
            .set("Authorization", userToken)
            .send({
                user : res.body._id,
                severity : 9,
                description : "New test worry",
                type : "Current",
                date : Date(2020, 2, 16)
            })
            .expect(201, done)
        });
    });
    it("Testing removal of an account", (done) => {
        //Begin therapist add patient, setting a link in the account to check that these are removed properly as well.
        agent.post("/api/profile/links")
        .set("Authorization", therapistToken)
        .send({email : "test@user.com"})
        .expect(201)
        .end(() => {

            agent.delete("/api/profile") //Delete the profile
            .set("Authorization", userToken)
            .expect(204)
            .end(() => {

                agent.get("/api/profile") //Get for user profile should now return unauthorized
                .set("Auhtorization", userToken)
                .expect(401)
                .end(() => {
                    agent.get("/api/profile") //Despite just adding, linkedAccounts should now be empty
                    .set("Authorization", therapistToken)
                    .end((error, res) => {
                        chai.expect(res.body.linkedAccounts).to.be.empty;
                        done()
                    });
                });
            });
        });
    });
    it("Testing user updating account", (done) => {
        var detailsToUpdate = {
            forename : "James",
            surname : "Jameson"
        }

        agent.put("/api/profile")
        .set("Authorization", userToken)
        .send(detailsToUpdate)
        .expect(204)
        .end((error, res) => {
            agent.get("/api/profile")
            .set("Authorization", userToken)
            .expect(200)
            .end((error, res) => {
                chai.expect(res.body.forename).to.equal("James")
                chai.expect(res.body.surname).to.equal("Jameson")
                done()
            }) 
        })
    });
    it("Testing therapist updating account", (done) => {
        var detailsToUpdate = {
            forename : "Richard",
            surname : "Richardson"
        }

        agent.put("/api/profile")
        .set("Authorization", therapistToken)
        .send(detailsToUpdate)
        .expect(204)
        .end((error, res) => {
            agent.get("/api/profile")
            .set("Authorization", therapistToken)
            .expect(200)
            .end((error, res) => {
                chai.expect(res.body.forename).to.equal("Richard")
                chai.expect(res.body.surname).to.equal("Richardson")
                done()
            }) 
        })
    });
    it("Testing user add worry solution", (done) => {
        var solutionToAdd = {
            description: "testdesc",
            advantages: "testadv",
            disadvantages: "testdisadv"
        }

        var date = new Date(2020, 2, 23)

        agent.post("/api/profile/worries/" + date.toISOString() + "/solutions")
        .set("Authorization", userToken)
        .send(solutionToAdd)
        .expect(204, done)

    });
    it("Testing user add worry solution with wrong date", (done) => {
        var solutionToAdd = {
            description: "testdesc",
            advantages: "testadv",
            disadvantages: "testdisadv"
        }

        var date = new Date(2020, 2, 4)

        agent.post("/api/profile/worries/" + date.toISOString() + "/solutions")
        .set("Authorization", userToken)
        .send(solutionToAdd)
        .expect(404, done)

    });
    it("Testing user delete mood", (done) => {
        var date = new Date(2020, 2, 23)

        agent.delete("/api/profile/moods/" + date.toISOString())
        .set("Authorization", userToken)
        .expect(204, done)
    });
    it("Testing user delete worry", (done) => {
        var date = new Date(2020, 2, 23)

        agent.delete("/api/profile/worries/" + date.toISOString())
        .set("Authorization", userToken)
        .expect(204, done)
    });
///////////////End of tests ///////////////////
    after(() => {

    })
})

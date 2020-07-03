const express = require('express');
const router = express.Router();
const roles = require("../config/roles");

//Controllers
const profile = require("./api/profile");
const therapist = require("./api/therapists");
const mood = require("./api/moods");
const link = require("./api/links");
const patient = require("./api/patients");
const worries = require("./api/worries")

// Profile - No virtuals
router.get("/profile", roles.checkUserHasRole("patient", "therapist"), profile.getProfile);
router.put("/profile", roles.checkUserHasRole("patient", "therapist"), profile.updateProfile);
router.delete("/profile", roles.checkUserHasRole("patient", "therapist"), profile.deleteProfile);

// Profile links
router.post("/profile/links", roles.checkUserHasRole("therapist"), link.addNewLink)
router.get("/profile/links", roles.checkUserHasRole("patient", "therapist"), link.getMyLinks);
router.put("/profile/link/:user", roles.checkUserHasRole("patient"), link.acceptLink)
router.delete("/profile/link/:user", roles.checkUserHasRole("patient", "therapist"), link.removeLink);

// Create/Read moods as the user
router.get("/profile/moods", roles.checkUserHasRole("patient"), mood.getMoodsAsPatient);
router.post("/profile/moods", roles.checkUserHasRole("patient"), mood.addNewMood);
router.delete("/profile/moods/:date", roles.checkUserHasRole("patient"), mood.deleteMood);

// Create/Read worries as the user
router.get("/profile/worries", roles.checkUserHasRole("patient"), worries.getWorriesAsPatient);
router.post("/profile/worries", roles.checkUserHasRole("patient"), worries.addNewWorry);
router.delete("/profile/worries/:date", roles.checkUserHasRole("patient"), worries.deleteWorry);

//Create worry solution as User
router.post("/profile/worries/:date/solutions", roles.checkUserHasRole("patient"), worries.addSolutionToWorry);


// Get all data for a patient
router.get("/patient/:id", roles.checkUserHasRole("therapist"), patient.getPatientDataAsTherapist);



module.exports = router;
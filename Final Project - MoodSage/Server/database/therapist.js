const mongoose = require("mongoose");
const Account = require("./account").Account;
const User = require("./user").User;

therapistSchema = new mongoose.Schema({
    linkedAccounts : [{
        user : {
            type : mongoose.Schema.Types.ObjectId,
            ref : 'Users',
            required : true 
        },
        status : {
            type : Number,
            default : 0,
            enum : [0,1] // 0 - Pending, 1 - Accepted. Account links will be deleted on rejection/removal
        }
    }]
}, {
toJSON : {
    virtuals : true
},
toObject : {
    virtuals : true
}
})

therapistSchema.pre('remove', async function(next) {
    var removedUser = this;
    
    for (const link of removedUser.linkedAccounts) {
        await User.updateOne(
            {_id : link.user._id},
            {$pull : { linkedAccounts : {user : removedUser._id }}},
            {safe : true}
        )
    }
    next()
})

var therapistDiscriminator = Account.discriminator('Therapists', therapistSchema);

module.exports.Therapist = therapistDiscriminator
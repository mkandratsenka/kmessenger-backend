db = db.getSiblingDB('profile_db');

db.profiles.createIndex({email: 1}, {unique: true});
db.profiles.createIndex({usernameLower: 1}, {unique: true});
db.profiles.createIndex({fullNameLower: 1});

print("Finished 'profile_db' migration.");
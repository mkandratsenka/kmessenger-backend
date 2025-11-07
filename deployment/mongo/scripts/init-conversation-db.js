db = db.getSiblingDB('conversation_db');

db.conversations.createIndex({uniqueKey: 1}, {unique: true, sparse: true});
db.conversations.createIndex({participants: 1, lastMessageDate: -1});

db.conversation_previews.createIndex({conversationId: 1, userId: 1});
db.conversation_previews.createIndex({userId: 1});

db.messages.createIndex({conversationId: 1, seq: -1});

print("Finished 'conversation_db' migration.");
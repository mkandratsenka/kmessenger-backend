package com.kondr.conversation.persistence.repository;

import com.kondr.conversation.persistence.domain.ConversationDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

/**
 * Репозиторий для работы с документами диалогов в MongoDB.
 */
public interface ConversationRepository
    extends ReactiveMongoRepository<ConversationDocument, ObjectId> {

}
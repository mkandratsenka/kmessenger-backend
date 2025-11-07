package com.kondr.conversation.persistence.repository;

import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * Репозиторий для работы с документами превью диалогов в MongoDB.
 */
public interface ConversationPreviewRepository
    extends ReactiveMongoRepository<ConversationPreviewDocument, ObjectId> {

  Mono<Boolean> existsByIdAndUserId(ObjectId id, String userId);

  Mono<Boolean> existsByConversationIdAndUserId(ObjectId conversationId, String userId);

  Mono<ConversationPreviewDocument> findFirstByConversationIdAndUserId(ObjectId conversationId,
      String userId);

}
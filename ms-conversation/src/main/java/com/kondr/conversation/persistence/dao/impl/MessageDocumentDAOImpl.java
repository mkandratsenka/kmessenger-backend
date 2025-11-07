package com.kondr.conversation.persistence.dao.impl;

import com.kondr.conversation.persistence.constant.MessageFields;
import com.kondr.conversation.persistence.dao.MessageDocumentDAO;
import com.kondr.conversation.persistence.domain.MessageDocument;
import com.kondr.conversation.persistence.repository.MessageRepository;
import com.kondr.mongo.mapper.ObjectIdMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MessageDocumentDAOImpl implements MessageDocumentDAO {

  private static final int MAX_FETCH_LIMIT = 1000;

  private final ObjectIdMapper objectIdMapper;

  private final MessageRepository messageRepository;

  private final ReactiveMongoTemplate mongoTemplate;

  @Override
  public Mono<MessageDocument> create(MessageDocument message) {
    return messageRepository.insert(message);
  }

  @Override
  public Flux<MessageDocument> findMessagesBeforeSeq(String conversationId, Long seq, int limit) {
    if (StringUtils.isBlank(conversationId) || (seq == null) || (limit <= 0)) {
      return Flux.empty();
    }

    Query query = new Query();

    ObjectId conversationObjectId = objectIdMapper.toObjectId(conversationId);
    query.addCriteria(Criteria.where(MessageFields.CONVERSATION_ID).is(conversationObjectId));
    query.addCriteria(Criteria.where(MessageFields.SEQ).lt(seq));
    query.with(Sort.by(Sort.Direction.DESC, MessageFields.SEQ));
    query.limit(Math.min(limit, MAX_FETCH_LIMIT));

    return mongoTemplate.find(query, MessageDocument.class);
  }

  @Override
  public Flux<MessageDocument> findMessagesAfterSeq(String conversationId, Long seq, int limit) {
    if (StringUtils.isBlank(conversationId) || (seq == null) || (limit <= 0)) {
      return Flux.empty();
    }

    Query query = new Query();

    ObjectId conversationObjectId = objectIdMapper.toObjectId(conversationId);
    query.addCriteria(Criteria.where(MessageFields.CONVERSATION_ID).is(conversationObjectId));
    query.addCriteria(Criteria.where(MessageFields.SEQ).gt(seq));
    query.with(Sort.by(Sort.Direction.ASC, MessageFields.SEQ));
    query.limit(Math.min(limit, MAX_FETCH_LIMIT));

    return mongoTemplate.find(query, MessageDocument.class);
  }

}
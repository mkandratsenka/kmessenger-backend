package com.kondr.conversation.persistence.dao.impl;

import static com.kondr.conversation.persistence.constant.ConversationCollectionNames.CONVERSATIONS;
import static com.kondr.conversation.persistence.constant.ConversationCollectionNames.CONVERSATION_PREVIEWS;
import static com.kondr.mongo.utils.AggregationUtils.eqFieldToVal;
import static com.kondr.mongo.utils.AggregationUtils.eqFieldToVar;
import static com.kondr.mongo.utils.AggregationUtils.fieldPath;
import static com.kondr.mongo.utils.AggregationUtils.fieldRef;

import com.kondr.conversation.persistence.constant.ConversationFields;
import com.kondr.conversation.persistence.constant.ConversationPreviewFields;
import com.kondr.conversation.persistence.dao.ConversationPreviewDocumentDAO;
import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.domain.projection.ConversationPreviewProjection;
import com.kondr.conversation.persistence.repository.ConversationPreviewRepository;
import com.kondr.mongo.constant.DocumentFields;
import com.kondr.mongo.mapper.ObjectIdMapper;
import com.kondr.mongo.utils.AggregationUtils;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.BooleanOperators;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ConversationPreviewDocumentDAOImpl implements ConversationPreviewDocumentDAO {

  private static final String PREVIEW_ALIAS = "preview";
  private static final String CONV_ID_VAR = "convId";

  private final ConversationPreviewRepository conversationPreviewRepository;

  private final ReactiveMongoTemplate mongoTemplate;

  private final ObjectIdMapper objectIdMapper;

  @Override
  public Mono<Boolean> existsByIdAndUserId(String id, String userId) {
    return StringUtils.isBlank(id) || StringUtils.isBlank(userId)
        ? Mono.just(Boolean.FALSE)
        : conversationPreviewRepository
            .existsByIdAndUserId(objectIdMapper.toObjectId(id), userId);
  }

  @Override
  public Mono<Boolean> existsByConversationIdAndUserId(String conversationId, String userId) {
    return StringUtils.isBlank(userId) || StringUtils.isBlank(conversationId)
        ? Mono.just(Boolean.FALSE)
        : conversationPreviewRepository
            .existsByConversationIdAndUserId(objectIdMapper.toObjectId(conversationId), userId);
  }

  @Override
  public Flux<ConversationPreviewDocument> createAll(List<ConversationPreviewDocument> previews) {
    return conversationPreviewRepository.insert(previews);
  }

  @Override
  public Flux<ConversationPreviewProjection> findPreviewProjectionsByUserId(String userId) {
    if (StringUtils.isBlank(userId)) {
      return Flux.empty();
    }

    Aggregation aggregation = Aggregation.newAggregation(
        // 1. Фильтруем conversations по userId
        Aggregation.match(Criteria.where(ConversationFields.PARTICIPANTS).is(userId)),

        // 2. Джойним с коллекцией conversation_previews по conversationID
        LookupOperation.newLookup()
            .from(CONVERSATION_PREVIEWS)
            .let(AggregationUtils.variableForField(CONV_ID_VAR, DocumentFields.ID))
            .pipeline(
                Aggregation.match(
                    Criteria.expr(
                        BooleanOperators.And.and(
                            eqFieldToVar(ConversationPreviewFields.CONVERSATION_ID, CONV_ID_VAR),
                            eqFieldToVal(ConversationPreviewFields.USER_ID, userId)
                        )
                    )
                ),
                Aggregation.project().andInclude(
                    DocumentFields.ID,
                    ConversationPreviewFields.INTERLOCUTOR,
                    ConversationPreviewFields.READ_MESSAGE_SEQ
                )
            )
            .as(PREVIEW_ALIAS),

        // 3. Разворачиваем массив conversation (так как $lookup возвращает массив)
        Aggregation.unwind(fieldRef(PREVIEW_ALIAS), true),

        // 4. Проецируем результат
        Aggregation.project()
            .and(DocumentFields.ID)
            .as(ConversationPreviewProjection.CONVERSATION_ID)

            .and(ConversationFields.LAST_MESSAGE_PREVIEW)
            .as(ConversationPreviewProjection.LAST_MESSAGE_PREVIEW)

            .and(ConversationFields.LAST_MESSAGE_DATE)
            .as(ConversationPreviewProjection.LAST_MESSAGE_DATE)

            .and(ConversationFields.LAST_MESSAGE_SEQ)
            .as(ConversationPreviewProjection.LAST_MESSAGE_SEQ)

            .and(fieldPath(PREVIEW_ALIAS, DocumentFields.ID))
            .as(ConversationPreviewProjection.PREVIEW_ID)

            .and(fieldPath(PREVIEW_ALIAS, ConversationPreviewFields.INTERLOCUTOR))
            .as(ConversationPreviewProjection.INTERLOCUTOR)

            .and(fieldPath(PREVIEW_ALIAS, ConversationPreviewFields.READ_MESSAGE_SEQ))
            .as(ConversationPreviewProjection.READ_MESSAGE_SEQ)

            .andExclude(DocumentFields.ID)
    );

    return mongoTemplate
        .aggregate(aggregation, CONVERSATIONS, ConversationPreviewProjection.class);
  }

  @Override
  public Mono<ConversationPreviewDocument> findFirstPreview(String conversationId, String userId) {
    return StringUtils.isBlank(conversationId) || StringUtils.isBlank(userId)
        ? Mono.empty()
        : conversationPreviewRepository
            .findFirstByConversationIdAndUserId(objectIdMapper.toObjectId(conversationId), userId);
  }

  @Override
  public Mono<Instant> commitReadSeq(String id, Long readSeq) {
    if (StringUtils.isBlank(id) || (readSeq == null)) {
      return Mono.empty();
    }

    Criteria criteria = Criteria
        .where(DocumentFields.ID).is(objectIdMapper.toObjectId(id));

    return commitReadSeq(criteria, readSeq);
  }

  @Override
  public Mono<Instant> commitReadSeq(ObjectId conversationId, String userId, Long readSeq) {
    if ((conversationId == null) || StringUtils.isBlank(userId) || (readSeq == null)) {
      return Mono.empty();
    }

    Criteria criteria = Criteria
        .where(ConversationPreviewFields.CONVERSATION_ID).is(conversationId)
        .and(ConversationPreviewFields.USER_ID).is(userId);

    return commitReadSeq(criteria, readSeq);
  }

  private Mono<Instant> commitReadSeq(Criteria criteria, Long readSeq) {
    if (readSeq <= 0) {
      return Mono.error(new IllegalArgumentException("readSeq must be positive"));
    }

    Query query = new Query(
        criteria.and(ConversationPreviewFields.READ_MESSAGE_SEQ).lt(readSeq)
    );

    Instant lastReadDate = Instant.now();
    Update update = new Update()
        .set(ConversationPreviewFields.READ_MESSAGE_SEQ, readSeq)
        .set(ConversationPreviewFields.LAST_READ_DATE, lastReadDate);

    return mongoTemplate.updateFirst(query, update, ConversationPreviewDocument.class)
        .flatMap(result ->
            result.getModifiedCount() > 0
                ? Mono.just(lastReadDate)
                : Mono.empty()
        );
  }

}
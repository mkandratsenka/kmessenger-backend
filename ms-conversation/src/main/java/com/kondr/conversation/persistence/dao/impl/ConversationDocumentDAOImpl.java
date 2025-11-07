package com.kondr.conversation.persistence.dao.impl;

import static com.kondr.mongo.utils.AggregationUtils.fieldRef;
import static org.apache.commons.lang.StringUtils.isBlank;

import com.kondr.conversation.persistence.constant.ConversationFields;
import com.kondr.conversation.persistence.dao.ConversationDocumentDAO;
import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.handler.ConversationDocumentHandler;
import com.kondr.conversation.persistence.repository.ConversationRepository;
import com.kondr.mongo.constant.DocumentFields;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationUpdate;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.aggregation.SetOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class ConversationDocumentDAOImpl implements ConversationDocumentDAO {

  private final ConversationDocumentHandler conversationDocumentHandler;

  private final ConversationRepository conversationRepository;

  private final ReactiveMongoTemplate mongoTemplate;

  @Override
  public Mono<ConversationDocument> create(ConversationDocument conversation) {
    return conversationDocumentHandler.handle(conversation)
        .flatMap(conversationRepository::insert);
  }

  @Override
  public Mono<ConversationDocument> updateOnNewMessage(ObjectId id, Instant newMessageDate,
      String newMessagePreview) {

    if (id == null) {
      return Mono.empty();
    }

    if ((newMessageDate == null) || isBlank(newMessagePreview)) {
      return Mono.error(new IllegalArgumentException(String.format(
          "Failed to update conversation %s: missing required field(s)", id)));
    }

    SetOperation updateOperation = SetOperation.builder()
        .set(ConversationFields.LAST_MESSAGE_PREVIEW).toValue(newMessagePreview)
        .and()
        .set(ConversationFields.LAST_MESSAGE_DATE).toValueOf(
            ConditionalOperators
                .when(Criteria.where(ConversationFields.LAST_MESSAGE_DATE).lt(newMessageDate))
                .then(newMessageDate)
                .otherwise(Fields.field(ConversationFields.LAST_MESSAGE_DATE))
        )
        .and()
        .set(ConversationFields.LAST_MESSAGE_SEQ).toValueOf(
            ArithmeticOperators.Add.valueOf(fieldRef(ConversationFields.LAST_MESSAGE_SEQ)).add(1)
        );

    return mongoTemplate.findAndModify(
        new Query(Criteria.where(DocumentFields.ID).is(id)),
        AggregationUpdate.update().set(updateOperation),
        FindAndModifyOptions.options().returnNew(true),
        ConversationDocument.class
    );
  }

}
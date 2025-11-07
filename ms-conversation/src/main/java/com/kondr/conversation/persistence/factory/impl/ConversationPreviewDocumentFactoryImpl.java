package com.kondr.conversation.persistence.factory.impl;

import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.domain.embedded.InterlocutorPreview;
import com.kondr.conversation.persistence.factory.ConversationPreviewDocumentFactory;
import java.time.Instant;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

@Component
public class ConversationPreviewDocumentFactoryImpl implements ConversationPreviewDocumentFactory {

  @Override
  public ConversationPreviewDocument build(ObjectId conversationId, String userId,
      InterlocutorPreview interlocutorPreview, Instant lastReadDate, Long readMessageSeq) {

    return ConversationPreviewDocument.builder()
        .conversationId(conversationId)
        .userId(userId)
        .interlocutor(interlocutorPreview)
        .lastReadDate(lastReadDate)
        .readMessageSeq(readMessageSeq)
        .build();
  }
}

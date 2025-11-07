package com.kondr.conversation.persistence.handler.impl;

import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.handler.ConversationDocumentHandler;
import com.kondr.conversation.persistence.validator.ConversationDocumentValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ConversationDocumentHandlerImpl implements ConversationDocumentHandler {

  private static final String ID_SEPARATOR = "_";

  private final ConversationDocumentValidator conversationDocumentValidator;

  @Override
  public Mono<ConversationDocument> handle(ConversationDocument conversation) {
    return conversationDocumentValidator.validate(conversation)
        .flatMap(this::prepareUniqueKey);
  }

  private Mono<ConversationDocument> prepareUniqueKey(ConversationDocument conversation) {
    List<String> participants = conversation.getParticipants();

    conversation.setUniqueKey(generateUniqueKey(participants.get(0), participants.get(1)));

    return Mono.just(conversation);
  }

  private String generateUniqueKey(String userId, String interlocutorId) {
    return (userId.compareTo(interlocutorId) <= 0)
        ? userId + ID_SEPARATOR + interlocutorId
        : interlocutorId + ID_SEPARATOR + userId;
  }

}
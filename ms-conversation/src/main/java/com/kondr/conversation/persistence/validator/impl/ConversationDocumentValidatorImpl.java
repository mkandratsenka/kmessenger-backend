package com.kondr.conversation.persistence.validator.impl;

import static org.springframework.util.CollectionUtils.isEmpty;

import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.validator.ConversationDocumentValidator;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ConversationDocumentValidatorImpl implements ConversationDocumentValidator {

  private static final int EXPECTED_PARTICIPANT_COUNT = 2;

  @Override
  public Mono<ConversationDocument> validate(ConversationDocument conversation) {
    if (conversation == null) {
      return Mono.error(new IllegalArgumentException("Conversation document cannot be null"));
    }

    List<String> participants = conversation.getParticipants();
    if (isEmpty(participants) || participants.size() != EXPECTED_PARTICIPANT_COUNT) {
      return Mono.error(new IllegalArgumentException(
          "Conversation must have " + EXPECTED_PARTICIPANT_COUNT + " participants"));
    }

    if (Objects.equals(participants.get(0), participants.get(1))) {
      return Mono.error(new IllegalArgumentException("Participants must be distinct"));
    }

    return Mono.just(conversation);
  }

}
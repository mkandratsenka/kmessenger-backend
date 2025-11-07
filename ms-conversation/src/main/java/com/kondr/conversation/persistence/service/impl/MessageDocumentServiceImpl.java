package com.kondr.conversation.persistence.service.impl;

import com.kondr.conversation.dto.request.MessageBatchRequestDto;
import com.kondr.conversation.persistence.dao.MessageDocumentDAO;
import com.kondr.conversation.persistence.domain.MessageDocument;
import com.kondr.conversation.persistence.service.MessageDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MessageDocumentServiceImpl implements MessageDocumentService {

  private final MessageDocumentDAO messageDocumentDAO;

  @Override
  public Mono<MessageDocument> create(MessageDocument message) {
    return messageDocumentDAO.create(message);
  }

  @Override
  public Flux<MessageDocument> findMessages(String conversationId,
      MessageBatchRequestDto messageBatchRequest) {

    int count = messageBatchRequest.count();
    Long seq = messageBatchRequest.fromExclusiveSeq();
    int limit = Math.abs(count);

    return count < 0
        ? messageDocumentDAO.findMessagesBeforeSeq(conversationId, seq, limit)
        : messageDocumentDAO.findMessagesAfterSeq(conversationId, seq, limit);
  }

}
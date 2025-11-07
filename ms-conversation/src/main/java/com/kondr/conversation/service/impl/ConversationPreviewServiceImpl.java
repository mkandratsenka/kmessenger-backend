package com.kondr.conversation.service.impl;

import com.kondr.conversation.persistence.service.ConversationPreviewDocumentService;
import com.kondr.conversation.service.ConversationPreviewService;
import com.kondr.mongo.utils.MongoExceptionUtils;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@Service
@RequiredArgsConstructor
public class ConversationPreviewServiceImpl implements ConversationPreviewService {

  private final ConversationPreviewDocumentService conversationPreviewDocumentService;

  @Override
  public Mono<Instant> commitReadSeq(String id, Long readSeq) {
    return conversationPreviewDocumentService.commitReadSeq(id, readSeq)
        .retryWhen(Retry.backoff(3, Duration.ofMillis(30))
            .filter(MongoExceptionUtils::isWriteConflictException)
        );
  }

}
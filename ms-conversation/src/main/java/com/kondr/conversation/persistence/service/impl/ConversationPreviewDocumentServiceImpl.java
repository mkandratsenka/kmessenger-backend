package com.kondr.conversation.persistence.service.impl;

import com.kondr.conversation.persistence.dao.ConversationPreviewDocumentDAO;
import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.domain.projection.ConversationPreviewProjection;
import com.kondr.conversation.persistence.service.ConversationPreviewDocumentService;
import com.kondr.web.security.service.CurrentUserService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ConversationPreviewDocumentServiceImpl implements ConversationPreviewDocumentService {

  private final CurrentUserService currentUserService;

  private final ConversationPreviewDocumentDAO conversationPreviewDocumentDAO;

  @Override
  public Flux<ConversationPreviewDocument> createAll(List<ConversationPreviewDocument> previews) {
    return conversationPreviewDocumentDAO.createAll(previews);
  }

  @Override
  public Flux<ConversationPreviewProjection> findCurrentUserConversationPreviews() {
    return currentUserService.getIdOrError()
        .flatMapMany(conversationPreviewDocumentDAO::findPreviewProjectionsByUserId);
  }

  @Override
  public Mono<ConversationPreviewDocument> findCurrentUserConversationPreview(
      String conversationId) {

    return currentUserService.getIdOrError()
        .flatMap(userId -> conversationPreviewDocumentDAO.findFirstPreview(conversationId, userId));
  }

  @Override
  public Mono<Instant> commitReadSeq(String id, Long readSeq) {
    return conversationPreviewDocumentDAO.commitReadSeq(id, readSeq);
  }

  @Override
  public Mono<Instant> commitReadSeq(ObjectId conversationId, String userId, Long readSeq) {
    return conversationPreviewDocumentDAO.commitReadSeq(conversationId, userId, readSeq);
  }

}
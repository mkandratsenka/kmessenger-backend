package com.kondr.conversation.security.checker.impl;

import com.kondr.conversation.context.RSocketConnectionContext;
import com.kondr.conversation.manager.RSocketConnectionContextManager;
import com.kondr.conversation.persistence.dao.ConversationPreviewDocumentDAO;
import com.kondr.conversation.security.checker.ConversationPreviewAccessChecker;
import com.kondr.web.security.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component(ConversationPreviewAccessCheckerImpl.NAME)
@RequiredArgsConstructor
public class ConversationPreviewAccessCheckerImpl implements ConversationPreviewAccessChecker {

  public static final String NAME = "conversationPreviewAccessCheckerImpl";

  private final RSocketConnectionContextManager rSocketConnectionContextManager;

  private final CurrentUserService currentUserService;

  private final ConversationPreviewDocumentDAO conversationPreviewDocumentDAO;

  @Override
  public Mono<Boolean> hasAccess(String conversationPreviewId, RSocketRequester requester) {
    RSocketConnectionContext context = rSocketConnectionContextManager.getContext(requester);
    if (context == null) {
      return Mono.just(Boolean.FALSE);
    }

    return context.hasAccessToConversationPreview(conversationPreviewId)
        ? Mono.just(Boolean.TRUE)
        : currentUserService.getId()
            .flatMap(userId -> conversationPreviewDocumentDAO
                .existsByIdAndUserId(conversationPreviewId, userId)
            )
            .map(hasAccess -> {
              if (hasAccess) {
                context.addAllowedConversationPreviewId(conversationPreviewId);
              }
              return hasAccess;
            });
  }

}

package com.kondr.conversation.security.checker.impl;

import com.kondr.conversation.context.RSocketConnectionContext;
import com.kondr.conversation.manager.RSocketConnectionContextManager;
import com.kondr.conversation.persistence.dao.ConversationPreviewDocumentDAO;
import com.kondr.conversation.security.checker.ConversationAccessChecker;
import com.kondr.web.security.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component(ConversationAccessCheckerImpl.NAME)
@RequiredArgsConstructor
public class ConversationAccessCheckerImpl implements ConversationAccessChecker {

  public static final String NAME = "conversationAccessCheckerImpl";

  private final RSocketConnectionContextManager rSocketConnectionContextManager;

  private final CurrentUserService currentUserService;

  private final ConversationPreviewDocumentDAO conversationPreviewDocumentDAO;

  @Override
  public Mono<Boolean> hasAccess(String conversationId, RSocketRequester requester) {
    RSocketConnectionContext context = rSocketConnectionContextManager.getContext(requester);
    if (context == null) {
      return Mono.just(Boolean.FALSE);
    }

    return context.hasAccessToConversation(conversationId)
        ? Mono.just(Boolean.TRUE)
        : currentUserService.getId()
            .flatMap(userId -> conversationPreviewDocumentDAO
                .existsByConversationIdAndUserId(conversationId, userId)
            )
            .map(hasAccess -> {
              if (hasAccess) {
                context.addAllowedConversationId(conversationId);
              }
              return hasAccess;
            });
  }

}
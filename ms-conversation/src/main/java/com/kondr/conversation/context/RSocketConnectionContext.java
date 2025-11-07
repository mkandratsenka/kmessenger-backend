package com.kondr.conversation.context;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Контекст, связанный с одним активным RSocket-соединением.
 * <p>
 * Предназначен для хранения, кеширования и отслеживания ресурсов, с которыми осуществляется работа
 * в рамках соответствующего RSocket-соединения.
 */
public class RSocketConnectionContext {

  private final Set<String> allowedConversationIds = ConcurrentHashMap.newKeySet();
  private final Set<String> allowedConversationPreviewIds = ConcurrentHashMap.newKeySet();

  public boolean hasAccessToConversation(String conversationId) {
    return allowedConversationIds.contains(conversationId);
  }

  public void addAllowedConversationId(String conversationId) {
    allowedConversationIds.add(conversationId);
  }

  public boolean hasAccessToConversationPreview(String previewId) {
    return allowedConversationPreviewIds.contains(previewId);
  }

  public void addAllowedConversationPreviewId(String previewId) {
    allowedConversationPreviewIds.add(previewId);
  }

}
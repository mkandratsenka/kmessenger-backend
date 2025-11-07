package com.kondr.conversation.manager.impl;

import com.kondr.conversation.context.RSocketConnectionContext;
import com.kondr.conversation.manager.RSocketConnectionContextManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

@Component
public class RSocketConnectionContextManagerImpl implements RSocketConnectionContextManager {

  private final Map<RSocketRequester, RSocketConnectionContext> connectionsMap = new ConcurrentHashMap<>();

  @Override
  public void register(RSocketRequester requester) {
    if (requester != null) {
      connectionsMap.put(requester, new RSocketConnectionContext());
    }
  }

  @Override
  public RSocketConnectionContext getContext(RSocketRequester requester) {
    return connectionsMap.get(requester);
  }

  @Override
  public void unregister(RSocketRequester requester) {
    if (requester != null) {
      connectionsMap.remove(requester);
    }
  }

}
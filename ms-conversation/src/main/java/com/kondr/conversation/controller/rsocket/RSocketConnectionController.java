package com.kondr.conversation.controller.rsocket;

import com.kondr.conversation.manager.RSocketConnectionContextManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RSocketConnectionController {

  private final RSocketConnectionContextManager rSocketConnectionContextManager;

  @SuppressWarnings("ConstantConditions")
  @ConnectMapping
  public Mono<Void> onConnect(RSocketRequester requester) {
    rSocketConnectionContextManager.register(requester);

    log.info("Connection established: {}", requester);

    requester.rsocket()
        .onClose()
        .doOnError(error -> log.error("Connection error: {}", error.getMessage()))
        .doFinally(signalType -> rSocketConnectionContextManager.unregister(requester))
        .subscribe(); // необходим для активации onClose при закрытии соединения

    return Mono.empty();
  }

}
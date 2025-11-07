package com.kondr.conversation.controller.rsocket;

import com.kondr.conversation.dto.response.ConversationPreviewResponseDto;
import com.kondr.conversation.dto.response.FullConversationPreviewResponseDto;
import com.kondr.conversation.dto.response.ReadSeqCommitResponseDto;
import com.kondr.conversation.mapper.ConversationPreviewMapper;
import com.kondr.conversation.persistence.service.ConversationPreviewDocumentService;
import com.kondr.conversation.service.ConversationPreviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ConversationPreviewRSocketController {

  private final ConversationPreviewService conversationPreviewService;

  private final ConversationPreviewDocumentService conversationPreviewDocumentService;

  private final ConversationPreviewMapper conversationPreviewMapper;

  @MessageMapping("me.conversations.previews.full")
  public Mono<List<FullConversationPreviewResponseDto>> getCurrentUserConversationPreviews() {
    return conversationPreviewDocumentService.findCurrentUserConversationPreviews()
        .map(conversationPreviewMapper::toFullDto)
        .collectList();
  }

  @PreAuthorize("@conversationAccessCheckerImpl.hasAccess(#conversationId, #requester)")
  @MessageMapping("conversations.{conversationId}.preview")
  public Mono<ConversationPreviewResponseDto> getConversationPreview(
      @DestinationVariable String conversationId,
      RSocketRequester requester) {

    return conversationPreviewDocumentService.findCurrentUserConversationPreview(conversationId)
        .map(conversationPreviewMapper::toDto);
  }

  @PreAuthorize("@conversationPreviewAccessCheckerImpl.hasAccess(#previewId, #requester)")
  @MessageMapping("conversations.previews.{previewId}.seq.commit")
  public Mono<ReadSeqCommitResponseDto> commitReadSeq(
      @DestinationVariable String previewId,
      @Payload Long readSeq,
      RSocketRequester requester) {

    return conversationPreviewService.commitReadSeq(previewId, readSeq)
        .map(ReadSeqCommitResponseDto::new)
        .defaultIfEmpty(ReadSeqCommitResponseDto.empty());
  }

}
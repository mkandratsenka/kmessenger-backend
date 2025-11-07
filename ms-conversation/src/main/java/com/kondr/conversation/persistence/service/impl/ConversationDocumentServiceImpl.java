package com.kondr.conversation.persistence.service.impl;

import static com.kondr.web.client.constant.ClientNames.PROFILE_SERVICE_CLIENT;

import com.kondr.conversation.dto.request.ConversationRequestDto;
import com.kondr.conversation.mapper.InterlocutorPreviewMapper;
import com.kondr.conversation.persistence.dao.ConversationDocumentDAO;
import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.domain.MessageDocument;
import com.kondr.conversation.persistence.domain.model.ConversationCreationResult;
import com.kondr.conversation.persistence.factory.ConversationPreviewDocumentFactory;
import com.kondr.conversation.persistence.service.ConversationDocumentService;
import com.kondr.conversation.persistence.service.ConversationPreviewDocumentService;
import com.kondr.conversation.persistence.service.MessageDocumentService;
import com.kondr.conversation.utils.MessageUtils;
import com.kondr.web.client.clients.ProfileClient;
import com.kondr.shared.dto.profile.PublicProfileResponseDto;
import com.kondr.web.security.service.CurrentUserService;
import java.time.Instant;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Service
public class ConversationDocumentServiceImpl implements ConversationDocumentService {

  private static final Long INITIAL_MESSAGE_SEQ = 1L;

  private final ProfileClient profileServiceClient;

  private final CurrentUserService currentUserService;

  private final ConversationPreviewDocumentFactory conversationPreviewDocumentFactory;

  private final ConversationPreviewDocumentService conversationPreviewDocumentService;

  private final MessageDocumentService messageDocumentService;

  private final InterlocutorPreviewMapper interlocutorPreviewMapper;

  private final TransactionalOperator transactionalOperator;

  private final ConversationDocumentDAO conversationDocumentDAO;

  public ConversationDocumentServiceImpl(
      @Qualifier(PROFILE_SERVICE_CLIENT) ProfileClient profileServiceClient,
      CurrentUserService currentUserService,
      ConversationPreviewDocumentFactory conversationPreviewDocumentFactory,
      ConversationPreviewDocumentService conversationPreviewDocumentService,
      MessageDocumentService messageDocumentService,

      InterlocutorPreviewMapper interlocutorPreviewMapper,
      TransactionalOperator transactionalOperator,
      ConversationDocumentDAO conversationDocumentDAO) {

    this.profileServiceClient = profileServiceClient;
    this.currentUserService = currentUserService;
    this.conversationPreviewDocumentFactory = conversationPreviewDocumentFactory;
    this.conversationPreviewDocumentService = conversationPreviewDocumentService;
    this.messageDocumentService = messageDocumentService;
    this.interlocutorPreviewMapper = interlocutorPreviewMapper;
    this.transactionalOperator = transactionalOperator;
    this.conversationDocumentDAO = conversationDocumentDAO;
  }

  @Override
  public Mono<ConversationCreationResult> create(ConversationRequestDto conversationDto) {
    return currentUserService.getIdOrError()
        .flatMap(userId -> createConversation(userId, conversationDto));
  }

  @Override
  public Mono<ConversationDocument> updateOnNewMessage(MessageDocument message) {
    return message == null
        ? Mono.empty()
        : conversationDocumentDAO.updateOnNewMessage(
            message.getConversationId(),
            message.getSentAt(),
            MessageUtils.getMessagePreview(message.getContent())
        );
  }

  private Mono<ConversationCreationResult> createConversation(String userId,
      ConversationRequestDto conversationDto) {

    String interlocutorId = conversationDto.interlocutorId();
    List<String> participants = List.of(userId, interlocutorId);

    return profileServiceClient.getProfiles(participants)
        .collectList()
        .flatMap(profiles -> {
          var userProfile = findProfileInList(profiles, userId);
          var interlocutorProfile = findProfileInList(profiles, interlocutorId);
          if (userProfile == null || interlocutorProfile == null) {
            return Mono.error(new IllegalStateException("One of the profiles was not found"));
          }

          return conversationDocumentDAO.create(buildNewConversation(participants, conversationDto))
              .flatMap(conversation -> createPreviewsAndMessage(
                  conversation, conversationDto, userProfile, interlocutorProfile)
              )
              .as(transactionalOperator::transactional);
        });
  }

  private ConversationDocument buildNewConversation(List<String> participants,
      ConversationRequestDto conversationDto) {

    return ConversationDocument.builder()
        .participants(participants)
        .lastMessageSeq(INITIAL_MESSAGE_SEQ)
        .lastMessageDate(Instant.now())
        .lastMessagePreview(MessageUtils.getMessagePreview(conversationDto.initialMessageContent()))
        .build();
  }

  private Mono<ConversationCreationResult> createPreviewsAndMessage(
      ConversationDocument conversation, ConversationRequestDto conversationDto,
      PublicProfileResponseDto userProfile, PublicProfileResponseDto interlocutorProfile) {

    ObjectId conversationId = conversation.getId();
    Instant lastMessageDate = conversation.getLastMessageDate();

    String userId = userProfile.id();
    var interlocutorPreview = interlocutorPreviewMapper.from(interlocutorProfile);
    var userConversationPreview = conversationPreviewDocumentFactory
        .build(conversationId, userId, interlocutorPreview, lastMessageDate, INITIAL_MESSAGE_SEQ);

    String interlocutorId = interlocutorProfile.id();
    var userPreview = interlocutorPreviewMapper.from(userProfile);
    var interlocutorConversationPreview = conversationPreviewDocumentFactory
        .build(conversationId, interlocutorId, userPreview, Instant.EPOCH, 0L);

    var previews = List.of(userConversationPreview, interlocutorConversationPreview);

    MessageDocument initialMessage = buildInitialMessage(conversationId, userId, lastMessageDate,
        conversationDto.initialMessageContent());

    return Mono.zip(
            conversationPreviewDocumentService.createAll(previews).collectList(),
            messageDocumentService.create(initialMessage)
        )
        .map(previewsAndMessage ->
            ConversationCreationResult.builder()
                .conversation(conversation)
                .userConversationPreview(userConversationPreview)
                .message(previewsAndMessage.getT2())
                .build()
        );
  }

  private PublicProfileResponseDto findProfileInList(List<PublicProfileResponseDto> profiles,
      String id) {

    return profiles.stream()
        .filter(profileDto -> id.equals(profileDto.id()))
        .findFirst()
        .orElse(null);
  }

  private MessageDocument buildInitialMessage(ObjectId conversationId, String userId,
      Instant lastMessageDate, String content) {

    return MessageDocument.builder()
        .conversationId(conversationId)
        .senderId(userId)
        .sentAt(lastMessageDate)
        .seq(INITIAL_MESSAGE_SEQ)
        .content(content)
        .build();
  }

}
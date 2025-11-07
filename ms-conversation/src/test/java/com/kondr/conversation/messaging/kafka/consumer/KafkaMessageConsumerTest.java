package com.kondr.conversation.messaging.kafka.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.kondr.conversation.messaging.kafka.event.NewMessageEvent;
import com.kondr.conversation.messaging.kafka.producer.KafkaMessageProducer;
import com.kondr.conversation.messaging.redis.event.MessageCreatedEvent;
import com.kondr.conversation.messaging.redis.publisher.RedisMessagePublisher;
import com.kondr.conversation.persistence.dao.ConversationDocumentDAO;
import com.kondr.conversation.persistence.dao.ConversationPreviewDocumentDAO;
import com.kondr.conversation.persistence.domain.ConversationDocument;
import com.kondr.conversation.persistence.domain.ConversationPreviewDocument;
import com.kondr.conversation.persistence.repository.ConversationPreviewRepository;
import com.kondr.conversation.persistence.repository.ConversationRepository;
import com.kondr.conversation.persistence.repository.MessageRepository;
import com.kondr.mongo.mapper.ObjectIdMapper;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.data.mongodb.database=test"
})
public class KafkaMessageConsumerTest {

  private static final String CONVERSATION_ID_SUFFIX = "084feaf0138fef887120a";
  private static final String CONVERSATION_PREVIEW_ID_SUFFIX = "088feaf0138fef887120a";
  private static final String MESSAGE_ID_SUFFIX = "8feaf0138fef887120a";

  private static final int USER_NUM = 1;
  private static final int INTERLOCUTOR_NUM = 2;

  @Autowired
  private KafkaMessageProducer kafkaMessageProducer;

  @Autowired
  private ConversationRepository conversationRepository;

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private ConversationDocumentDAO conversationDocumentDAO;

  @Autowired
  private ConversationPreviewRepository conversationPreviewRepository;

  @Autowired
  private ConversationPreviewDocumentDAO conversationPreviewDocumentDAO;

  @Autowired
  private ObjectIdMapper objectIdMapper;

  @MockitoBean
  private RedisMessagePublisher redisMessagePublisher;

  @BeforeEach
  void setUp() {
    when(redisMessagePublisher.publish(any(MessageCreatedEvent.class), anyString()))
        .thenReturn(Mono.empty());
  }

  /**
   * Если групп больше, чем max-active-conversations, то обработка приостановится когда буфер групп
   * заполнится. По истечению conversation-idle-check-interval начнутся очистки и процесс
   * продолжится.
   */
  @Disabled
  @Test
  public void shouldProcessAllMessages() {
    int groupCount = 300; // если указать < max-active-conversations, то без подчистки
    int messages = 2000; // 99999 max, или менять формулу генерации ID

    for (int i = 0; i < groupCount; i++) {
      ObjectId conversationId = objectIdMapper.toObjectId(generateConversationId(i));

      String userId = generateUserId(USER_NUM, i);

      ConversationDocument conversation = ConversationDocument.builder()
          .id(conversationId)
          .participants(List.of(userId, generateUserId(INTERLOCUTOR_NUM, i)))
          .lastMessageSeq(0L)
          .build();

      conversationRepository.deleteById(conversationId).block();
      conversationDocumentDAO.create(conversation).block();

      ObjectId previewId = objectIdMapper.toObjectId(generateConversationPreviewId(i));

      ConversationPreviewDocument conversationPreview = ConversationPreviewDocument.builder()
          .id(previewId)
          .userId(userId)
          .conversationId(conversationId)
          .build();

      conversationPreviewRepository.deleteById(previewId).block();
      conversationPreviewDocumentDAO.createAll(List.of(conversationPreview)).blockLast();
    }

    clearMessages(messages);

    for (int i = 0; i < messages; i++) {
      String eventId = generateMessageId(i);

      NewMessageEvent newEvent = NewMessageEvent.builder()
          .tempId(eventId)
          .content(String.valueOf(i))
          .conversationId(generateConversationId(i % groupCount))
          .senderId(generateUserId(USER_NUM, i))
          .sentAt(Instant.now())
          .build();

      kafkaMessageProducer.send(newEvent).block();
    }

    try {
      Thread.sleep(120_000); // даем 2 минуты консюмеру или приложение завершится
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private String generateUserId(int n, int i) {
    return "user" + n + "_" + i;
  }

  private String generateConversationId(int i) {
    return String.format("%03d", i) + CONVERSATION_ID_SUFFIX;
  }

  private String generateConversationPreviewId(int i) {
    return String.format("%03d", i) + CONVERSATION_PREVIEW_ID_SUFFIX;
  }

  private String generateMessageId(int i) {
    return String.format("%05d", i) + MESSAGE_ID_SUFFIX;
  }

  private void clearMessages(int messages) {
    List<ObjectId> idsToDelete = IntStream.range(0, messages)
        .mapToObj(this::generateMessageId)
        .map(objectIdMapper::toObjectId)
        .collect(Collectors.toList());

    messageRepository.deleteAllById(idsToDelete).block();
  }

}
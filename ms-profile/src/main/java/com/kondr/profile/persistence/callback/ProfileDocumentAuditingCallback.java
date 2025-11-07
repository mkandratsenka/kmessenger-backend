package com.kondr.profile.persistence.callback;

import com.kondr.profile.persistence.domain.ProfileDocument;
import java.time.Instant;
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * {@code Callback}, выполняемый перед преобразованием {@link ProfileDocument} в формат, пригодный
 * для сохранения в MongoDB.
 */
@Component
public class ProfileDocumentAuditingCallback
    implements ReactiveBeforeConvertCallback<ProfileDocument> {

  /**
   * Вручную устанавливаем {@code createdAt}, так как {@code @CreatedDate} не работает, если
   * ID ({@code id}) установлен до первого сохранения документа.
   */
  @Override
  public @NonNull Mono<ProfileDocument> onBeforeConvert(ProfileDocument profile,
      @NonNull String collection) {

    if (profile.getCreatedAt() == null) {
      profile.setCreatedAt(Instant.now());
    }
    return Mono.just(profile);
  }
}